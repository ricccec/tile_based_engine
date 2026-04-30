package engine.events;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventBusTest {

    private EventBus bus;

    @Before
    public void setUp() {
        bus = new EventBus();
    }

    // -----------------------------------------------------------------------
    // Basic delivery
    // -----------------------------------------------------------------------

    @Test
    public void eventDeliveredOnDispatch() {
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.GAME_SHUTDOWN, received::add);

        bus.queue(new EvtGameShutdown());
        assertTrue("event must not be delivered before dispatch()", received.isEmpty());

        bus.dispatchEvents();
        assertEquals(1, received.size());
    }

    @Test
    public void multipleListenersSameType() {
        List<String> log = new ArrayList<>();
        bus.addListener(EventType.GAME_SHUTDOWN, e -> log.add("A"));
        bus.addListener(EventType.GAME_SHUTDOWN, e -> log.add("B"));

        bus.queue(new EvtGameShutdown());
        bus.dispatchEvents();

        assertEquals(List.of("A", "B"), log);
    }

    @Test
    public void typeIsolation() {
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.GAME_SHUTDOWN, received::add);

        bus.queue(new EvtDummy(42));
        bus.dispatchEvents();

        assertTrue("EvtDummy must not reach GAME_SHUTDOWN listener", received.isEmpty());
    }

    @Test
    public void correctPayloadDelivered() {
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.DUMMY, received::add);

        bus.queue(new EvtDummy(7));
        bus.dispatchEvents();

        assertEquals(1, received.size());
        assertEquals(7, ((EvtDummy) received.get(0)).value());
    }

    // -----------------------------------------------------------------------
    // Unregistration
    // -----------------------------------------------------------------------

    @Test
    public void listenerCanBeUnregistered() {
        List<IGameEvent> received = new ArrayList<>();
        EventListener listener = received::add;
        bus.addListener(EventType.GAME_SHUTDOWN, listener);

        bus.queue(new EvtGameShutdown());
        bus.dispatchEvents();
        assertEquals(1, received.size());

        assertTrue(bus.removeListener(EventType.GAME_SHUTDOWN, listener));

        bus.queue(new EvtGameShutdown());
        bus.dispatchEvents();
        assertEquals("listener must not receive events after unregistration", 1, received.size());
    }

    // -----------------------------------------------------------------------
    // queue() inside a listener is allowed — event arrives next tick
    // -----------------------------------------------------------------------

    @Test
    public void queueInsideListenerDeliveredNextTick() {
        List<Integer> log = new ArrayList<>();
        bus.addListener(EventType.DUMMY, e -> {
            int v = ((EvtDummy) e).value();
            log.add(v);
            if (v == 1) bus.queue(new EvtDummy(2));
        });

        bus.queue(new EvtDummy(1));
        bus.dispatchEvents();  // tick 1: delivers (1), listener queues (2)
        assertEquals("tick 1 must deliver only value 1", List.of(1), log);

        bus.dispatchEvents();  // tick 2: delivers (2)
        assertEquals("tick 2 must deliver value 2", List.of(1, 2), log);
    }

    @Test
    public void eventQueuedDuringDispatchNotSeenBySameTickListeners() {
        List<String> log = new ArrayList<>();

        bus.addListener(EventType.DUMMY, e -> {
            int v = ((EvtDummy) e).value();
            if (v == 1) {
                bus.queue(new EvtDummy(2));
                log.add("first-saw-" + v);
            }
        });
        bus.addListener(EventType.DUMMY, e -> log.add("second-saw-" + ((EvtDummy) e).value()));

        bus.queue(new EvtDummy(1));
        bus.dispatchEvents();  // tick 1

        assertEquals(
            "tick 1: both listeners see (1) only",
            List.of("first-saw-1", "second-saw-1"),
            log
        );

        bus.dispatchEvents();  // tick 2: (2) arrives
        assertEquals(
            "tick 2: second listener sees (2)",
            List.of("first-saw-1", "second-saw-1", "second-saw-2"),
            log
        );
    }

    // -----------------------------------------------------------------------
    // dispatchImmediate
    // -----------------------------------------------------------------------

    @Test
    public void dispatchImmediateBypassesBuffer() {
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.GAME_SHUTDOWN, received::add);

        bus.dispatchImmediate(new EvtGameShutdown());

        assertEquals("dispatchImmediate must deliver synchronously", 1, received.size());
    }

    @Test
    public void dispatchImmediateDoesNotAffectPendingQueue() {
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.DUMMY, received::add);

        bus.queue(new EvtDummy(1));
        bus.dispatchImmediate(new EvtDummy(99));

        assertEquals(1, received.size());
        assertEquals(99, ((EvtDummy) received.get(0)).value());

        bus.dispatchEvents();  // now the queued one arrives
        assertEquals(2, received.size());
        assertEquals(1, ((EvtDummy) received.get(1)).value());
    }

    // -----------------------------------------------------------------------
    // Thread safety — concurrent producers
    // -----------------------------------------------------------------------

    @Test
    public void concurrentProducersAllEventsDelivered() throws InterruptedException {
        int threadCount  = 8;
        int eventsEach   = 500;
        List<IGameEvent> received = new ArrayList<>();
        bus.addListener(EventType.DUMMY, received::add);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch done  = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int id = t;
            pool.submit(() -> {
                ready.countDown();
                try { ready.await(); } catch (InterruptedException ignored) {}
                for (int i = 0; i < eventsEach; i++) {
                    bus.queue(new EvtDummy(id * eventsEach + i));
                }
                done.countDown();
            });
        }

        done.await(5, TimeUnit.SECONDS);
        pool.shutdown();

        bus.dispatchEvents();

        assertEquals(threadCount * eventsEach, received.size());
    }

    // -----------------------------------------------------------------------
    // No listeners — must not throw
    // -----------------------------------------------------------------------

    @Test
    public void noListenerRegistered_doesNotThrow() {
        bus.queue(new EvtGameShutdown());
        bus.dispatchEvents(); // must be silent
    }
}
