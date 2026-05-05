package main_server.net;

import net.NetworkCallback;
import net.PacketHandler;
import net.PacketReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class NetworkService<S extends Session> implements Runnable {
    private static final Logger logger = Logger.getLogger(NetworkService.class);

    private final ServerSocketChannel serverChannel;
    private final Supplier<S> sessionFactory;
    private final Properties config;
    private final Selector selector;
    private final Map<SocketChannel, S> sessions = new HashMap<>();
    private final ConcurrentLinkedQueue<Runnable> pendingActions = new ConcurrentLinkedQueue<>();
    private final Map<Integer, PacketHandler<S>> handlers = new HashMap<>();
    private volatile boolean running = true;

    public NetworkService(ServerSocketChannel serverChannel, Supplier<S> sessionFactory, Properties config) throws IOException {
        this.serverChannel = serverChannel;
        this.sessionFactory = sessionFactory;
        this.config = config;
        this.selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void registerHandler(int packetType, PacketHandler<S> handler) {
        handlers.put(packetType, handler);
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Wait for incoming connection/data
                selector.select();

                // Handle channels w/ pending request
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    // Handle connection request or incoming data
                    if (key.isAcceptable()) {
                        acceptConnection();
                    } else if (key.isReadable()) {
                        readFromClient((SocketChannel) key.channel());
                    }
                }
                // Disconnect inactive clients
                checkTimeouts();
                // Handle pending actions
                drainPendingActions();
            } catch (Exception e) {
                logger.error("Error in network loop", e);
            }
        }
    }

    private void drainPendingActions() {
        Runnable action;
        while ((action = pendingActions.poll()) != null) {
            action.run();
        }
    }

    private void acceptConnection() throws IOException {
        SocketChannel client = serverChannel.accept();

        // Register channel
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        sessions.put(client, sessionFactory.get());
        logger.info("Accepted connection from " + client.getRemoteAddress());
    }

    private void readFromClient(SocketChannel client) {
        S session = sessions.get(client);
        if (session == null) return;

        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                disconnectClient(client);
                return;
            }
            // Read packet type from buffer and dispatch to the appropriate handler
            buffer.flip();
            PacketReader reader = new PacketReader(buffer);
            int packetType = reader.readInt();

            dispatchPacket(client, session, packetType, reader);
        } catch (Exception e) {
            logger.error("Error reading from client", e);
            disconnectClient(client);
        }
    }

    private void dispatchPacket(SocketChannel client, S session, int packetType, PacketReader reader) {
        PacketHandler<S> handler = handlers.get(packetType);
        if (handler == null) {
            logger.warn("No handler registered for packet type: " + packetType);
            return;
        }
        handler.handle(client, session, reader, new NetworkCallback() {
            @Override
            public void onSuccess() {
                pendingActions.add(() -> {
                    session.authenticated = true;
                    session.lastSeen = System.currentTimeMillis();
                });
                selector.wakeup();
            }

            @Override
            public void onFailure(FailureType type) {
                pendingActions.add(() -> {
                    if (type == FailureType.HARD) {
                        disconnectClient(client);
                    } else {
                        session.failedAttempts++;
                        if (session.failedAttempts >= 3) {
                            disconnectClient(client);
                        }
                    }
                });
                selector.wakeup();
            }
        });
    }

    /**
     * Disconnect non-authenticated clients when {@code login_timeout_seconds} elapses.
     */
    private void checkTimeouts() {
        long now = System.currentTimeMillis();
        int timeout = Integer.parseInt(config.getProperty("login_timeout_seconds")) * 1000;
        sessions.entrySet().removeIf(entry -> {
            Session session = entry.getValue();
            if (session.authenticated) return false;
            if ((now - session.lastSeen) <= timeout) return false;
            try {
                entry.getKey().close();
            } catch (Exception e) {
                logger.error("Error closing timed out connection", e);
            }
            return true;
        });
    }

    private void disconnectClient(SocketChannel client) {
        try {
            client.close();
            sessions.remove(client);
            logger.info("Disconnected client");
        } catch (Exception e) {
            logger.error("Error disconnecting client", e);
        }
    }

    public void stop() {
        running = false;
        selector.wakeup();
    }

}