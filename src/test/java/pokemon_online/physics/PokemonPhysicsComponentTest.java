package pokemon_online.physics;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;

/**
 * Tests for the IDLE → ACCELERATING → MOVING state machine inside
 * PokemonPhysicsComponent. Each test drives the state objects directly so that
 * a real GameWorld is not required.
 */
public class PokemonPhysicsComponentTest {

    private GameObject obj;
    private PokemonPhysicsComponent phyComp;

    @Before
    public void setUp() {
        obj = new GameObject();                       // ACTIVE, at (0,0), facing 0°
        phyComp = new PokemonPhysicsComponent(obj);   // wraps the same obj
    }

    // =========================================================================
    // IDLE state
    // =========================================================================

    @Test
    public void idle_noInput_returnsNull() {
        PkmnPhyStateIdle idle = new PkmnPhyStateIdle(phyComp);
        assertNull(idle.updateState(obj, null, 50, null));
    }

    @Test
    public void idle_frozenObject_returnsNull() {
        obj.setState(State.FROZEN);
        PkmnPhyStateIdle idle = new PkmnPhyStateIdle(phyComp);
        assertNull(idle.updateState(obj, null, 50, CardinalDirection.DIR_RIGHT));
    }

    @Test
    public void idle_inputMatchingFacingDirection_transitionsToMoving() {
        // Object already faces right (direction = 0° → DIR_RIGHT)
        obj.setFacingDirection(0);
        PkmnPhyStateIdle idle = new PkmnPhyStateIdle(phyComp);

        PkmnPhyState next = idle.updateState(obj, null, 50, CardinalDirection.DIR_RIGHT);

        assertTrue("Expected PkmnPhyStateMoving", next instanceof PkmnPhyStateMoving);
    }

    @Test
    public void idle_inputDifferentFromFacingDirection_transitionsToAccelerating() {
        // Object faces right, player presses UP
        obj.setFacingDirection(0);
        PkmnPhyStateIdle idle = new PkmnPhyStateIdle(phyComp);

        PkmnPhyState next = idle.updateState(obj, null, 50, CardinalDirection.DIR_UP);

        assertTrue("Expected PkmnPhyStateAccelerating", next instanceof PkmnPhyStateAccelerating);
    }

    @Test
    public void idle_inputDifferentFromFacingDirection_updatesFacingDirection() {
        // After the idle→accelerating transition the object should face the new direction
        obj.setFacingDirection(0); // facing RIGHT
        PkmnPhyStateIdle idle = new PkmnPhyStateIdle(phyComp);
        idle.updateState(obj, null, 50, CardinalDirection.DIR_DOWN);

        CardinalDirection newFacing = CardinalDirection.degree2direction(obj.getFacingDirection());
        assertTrue("Expected to face DOWN after input change", newFacing == CardinalDirection.DIR_DOWN);
    }

    // =========================================================================
    // ACCELERATING state
    // =========================================================================

    @Test
    public void accelerating_noInput_transitionsToIdle() {
        PkmnPhyStateAccelerating acc = new PkmnPhyStateAccelerating(phyComp);
        acc.enterState(null);

        PkmnPhyState next = acc.updateState(obj, null, 50, null);

        assertTrue("Expected PkmnPhyStateIdle", next instanceof PkmnPhyStateIdle);
    }

    @Test
    public void accelerating_frozenObject_transitionsToIdle() {
        obj.setState(State.FROZEN);
        PkmnPhyStateAccelerating acc = new PkmnPhyStateAccelerating(phyComp);
        acc.enterState(null);

        PkmnPhyState next = acc.updateState(obj, null, 50, CardinalDirection.DIR_RIGHT);

        assertTrue("Expected PkmnPhyStateIdle on FROZEN", next instanceof PkmnPhyStateIdle);
    }

    @Test
    public void accelerating_timerBelowThreshold_returnsNull() {
        // Threshold is 2 * MS_PER_UPDATE = 100 ms; supply 99 ms
        obj.setFacingDirection(0); // facing RIGHT
        PkmnPhyStateAccelerating acc = new PkmnPhyStateAccelerating(phyComp);
        acc.enterState(null);

        long dtBelowThreshold = 2 * Configuration.MS_PER_UPDATE - 1;
        PkmnPhyState next = acc.updateState(obj, null, dtBelowThreshold, CardinalDirection.DIR_RIGHT);

        assertNull("Expected null (not yet elapsed)", next);
    }

    @Test
    public void accelerating_timerReachesThreshold_transitionsToMoving() {
        // Drive the state with two ticks that together exceed the 100 ms threshold
        obj.setFacingDirection(0); // facing RIGHT
        PkmnPhyStateAccelerating acc = new PkmnPhyStateAccelerating(phyComp);
        acc.enterState(null);

        long halfThreshold = Configuration.MS_PER_UPDATE; // 50 ms each
        acc.updateState(obj, null, halfThreshold, CardinalDirection.DIR_RIGHT); // timer = 50 < 100
        PkmnPhyState next = acc.updateState(obj, null, halfThreshold, CardinalDirection.DIR_RIGHT); // timer = 100

        assertTrue("Expected PkmnPhyStateMoving after threshold elapsed",
                next instanceof PkmnPhyStateMoving);
    }

    @Test
    public void accelerating_directionChange_resetsTimer() {
        // After a direction change the accumulated time should reset so that
        // supplying exactly the original remainder does NOT yet trigger MOVING.
        obj.setFacingDirection(0); // facing RIGHT
        PkmnPhyStateAccelerating acc = new PkmnPhyStateAccelerating(phyComp);
        acc.enterState(null);

        long threshold = 2 * Configuration.MS_PER_UPDATE; // 100 ms

        // Advance timer to just below threshold
        acc.updateState(obj, null, threshold - 1, CardinalDirection.DIR_RIGHT);

        // Change direction — this should reset the timer
        // (obj still faces RIGHT, so pressing UP triggers the mismatch branch)
        PkmnPhyState afterChange = acc.updateState(obj, null, 1, CardinalDirection.DIR_UP);

        // 1 ms after reset is well below threshold, so the state must stay in ACCELERATING
        assertNull("Expected null (timer reset on direction change)", afterChange);
        assertTrue("Timer should be reset to 1", acc.timerMillisec == 1);
    }

    // =========================================================================
    // MOVING state
    // =========================================================================

    @Test
    public void moving_noInputAndAligned_transitionsToIdle() {
        // Object is at (0,0) — a cell-aligned position — and has no velocity.
        // With no controller input the component should stop and return IDLE.
        phyComp.setSpeedX(0);
        phyComp.setSpeedY(0);
        PkmnPhyStateMoving moving = new PkmnPhyStateMoving(phyComp);
        moving.enterState(null);

        PkmnPhyState next = moving.updateState(obj, null, 50, null);

        assertTrue("Expected PkmnPhyStateIdle when speed drops to 0",
                next instanceof PkmnPhyStateIdle);
    }

    @Test
    public void moving_frozenObjectAndAligned_transitionsToIdle() {
        // If the object is frozen and not mid-cell the component must return IDLE.
        obj.setState(State.FROZEN);
        phyComp.setSpeedX(0);
        phyComp.setSpeedY(0);
        PkmnPhyStateMoving moving = new PkmnPhyStateMoving(phyComp);
        moving.enterState(null);

        PkmnPhyState next = moving.updateState(obj, null, 50, CardinalDirection.DIR_RIGHT);

        assertTrue("Expected PkmnPhyStateIdle when FROZEN and aligned",
                next instanceof PkmnPhyStateIdle);
    }
}
