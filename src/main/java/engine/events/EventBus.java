package engine.events;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Global event bus.
 *
 * <h2>Threading model</h2>
 * <ul>
 *   <li>{@link #queue(IGameEvent)} is thread-safe and may be called from any
 *       thread (game-loop thread, NIO selector thread, persistence thread, …).
 *   <li>{@link #dispatchEvents()}, {@link #addListener}, {@link #removeListener}, and {@link #dispatchImmediate}
 *       must only be called from the <em>game-loop thread</em>.
 * </ul>
 *
 * <h2>Ordering guarantee</h2>
 * {@link #dispatchEvents()} drains the inbox into a local snapshot, then iterates
 * the snapshot. Any event queued during that iteration — by a listener or
 * another thread — lands back in the inbox and is delivered in the
 * <em>next</em> tick, keeping the call graph deterministic.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // register (game-loop thread)
 * bus.addListener(EventType.GAME_SHUTDOWN, e -> shutdown());
 *
 * // produce (any thread)
 * bus.queue(new EvtGameShutdown());
 *
 * // game-loop tick end
 * bus.dispatch();
 *
 * // unregister when no longer needed
 * bus.removeListener(EventType.GAME_SHUTDOWN, listener);
 * }</pre>
 */
public final class EventBus {

    // Thread-safe inbox — written by any thread, drained by the game-loop thread
    private final ConcurrentLinkedQueue<IGameEvent> inbox = new ConcurrentLinkedQueue<>();

    // Listeners keyed by EventType — game-loop thread only
    private final Map<EventType, List<EventListener>> listenerMap = new EnumMap<>(EventType.class);

    /**
     * Queue an event for delivery during the next {@link #dispatchEvents()} call.
     * Thread-safe; may be called from any thread, including from inside a listener.
     */
    public void queue(IGameEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        inbox.add(event);
    }

    /**
     * Register a listener for the given {@link EventType}.
     *
     * <p>Must be called from the game-loop thread only.
     *
     * @param type     the event type to listen for
     * @param listener called with each matching event during {@link #dispatchEvents()}
     * @return {@code true} if the listener was added (always {@code true} unless
     *         the list implementation rejects it)
     */
    public boolean addListener(EventType type, EventListener listener) {
        Objects.requireNonNull(type,     "type must not be null");
        Objects.requireNonNull(listener, "listener must not be null");
        boolean added = listenerMap
                            .computeIfAbsent(type, k -> new ArrayList<>())
                            .add(listener);
        return added;
    }

    public boolean removeListener(EventType type, EventListener listener) {
        List<EventListener> handlers = listenerMap.get(type);
        if (handlers != null) { return handlers.remove(listener); }
        return false;
    }

    /**
     * Drain the inbox and deliver all pending events to registered listeners.
     * Must be called once per tick from the game-loop thread only.
     */
    public void dispatchEvents() {
        // Copy items one at the time to avoid missing an event if a producer threas
        // inserts it in the gap between the copy and the clear,
        List<IGameEvent> batch = new ArrayList<>();
        IGameEvent e;
        while ((e = inbox.poll()) != null) {
            batch.add(e);
        }
        for (IGameEvent event : batch) {
            fire(event);
        }
    }

    /**
     * Immediately deliver an event to all matching listeners, bypassing the inbox.
     *
     * <p><strong>Discouraged.</strong> Prefer {@link #queue(IGameEvent)} to
     * preserve deterministic tick ordering. Must be called from the game-loop
     * thread only.
     */
    public void dispatchImmediate(IGameEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        fire(event);
    }

    // -----------------------------------------------------------------------

    private void fire(IGameEvent event) {
        List<EventListener> handlers = listenerMap.get(event.type());
        if (handlers == null || handlers.isEmpty()) {
            return;
        }
        // Snapshot to tolerate listeners that unregister themselves mid-iteration
        for (EventListener h : new ArrayList<>(handlers)) {
            h.onEvent(event);
        }
    }
}
