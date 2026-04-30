package engine.events;

/**
 * Marker interface for all game events.
 *
 * <p>All implementations must be immutable {@code record}s that return a
 * constant {@link EventType} from {@link #type()}.
 *
 * <p>Events flow through {@link EventBus}: producers call
 * {@link EventBus#queue(IGameEvent)} from any thread; the game-loop thread
 * calls {@link EventBus#dispatchEvents()} once per tick to deliver them to listeners.
 */
public interface IGameEvent {
    EventType type();
}
