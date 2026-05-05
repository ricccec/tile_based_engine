package net;

import java.nio.channels.SocketChannel;

public interface PacketHandler<S> {
    void handle(SocketChannel client, S session, PacketReader reader, NetworkCallback callback);
}
