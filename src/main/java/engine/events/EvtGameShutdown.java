package engine.events;

/**
 * Signals that the game is shutting down cleanly.
 *
 * <p>Subsystems that need to flush state (persistence, network) should listen
 * for this event and complete their work before the process exits.
 */
public record EvtGameShutdown() implements IGameEvent {
    @Override public EventType type() { return EventType.GAME_SHUTDOWN; }
}
