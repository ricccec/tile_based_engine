package engine.events;

/** Test-only event carrying an integer value. */
public record EvtDummy(int value) implements IGameEvent {
    @Override public EventType type() { return EventType.DUMMY; }
}
