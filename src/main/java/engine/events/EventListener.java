package engine.events;

/**
 * Listener for a specific {@link EventType}.
 *
 * <p>Register with {@link EventBus#addListener(EventType, EventListener)}.
 * Implementations may be lambdas, method references, or named classes.
 * Cast to the concrete event record inside {@code onEvent} when needed.
 */
@FunctionalInterface
public interface EventListener {
    void onEvent(IGameEvent event);
}
