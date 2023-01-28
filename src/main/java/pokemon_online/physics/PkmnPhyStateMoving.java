/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameObject.State;

/**
 * @author Cecchi
 *
 */
public class PkmnPhyStateMoving extends PkmnPhyState {

	public PkmnPhyStateMoving(PokemonPhysicsComponent phyComp) {
		super(phyComp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enterState(GameWorld world) {
		// TODO Auto-generated method stub

	}

	@Override
	public PkmnPhyState updateState(GameObject obj, GameWorld world, long dtMillisec, CardinalDirection ctrlerDir) {
		// FIXME dtMillisec is ignored
		
		// Complete any previous movement
		int residueDist = Configuration.PLAYER_SPEED;//phyComp.getSpeed();
		if (phyComp.isCrossingCells()) {
			// A movement from the previous tick is still ongoing
			assert(phyComp.getSpeed() > 0);
			assert(obj.getState() != State.OBJ_STATE_IDLE); // This object can't interact while moving
			
			// Complete the movement
			int prevPos = (phyComp.getCardinalMovingDir().isAlongX() ? obj.getX() : obj.getY());
			phyComp.moveOneCell(world, residueDist);
			phyComp.resolveCollision(world);
			int currPos = (phyComp.getCardinalMovingDir().isAlongX() ? obj.getX() : obj.getY());
			residueDist -= Math.abs(prevPos - currPos);
		} else if (obj.getState() != State.OBJ_STATE_IDLE) {
			// Someone else freeze the object during the previous frame
			return new PkmnPhyStateIdle(phyComp);
		}
		
		
		if (!phyComp.isCrossingCells())  { 	// The state of the object's controller gets read only when the object reach the next cell
											// Depending on the controller's state the object can either stop or change direction
			if ((ctrlerDir == null)) {
				phyComp.setSpeedX(0);
				phyComp.setSpeedY(0);
				obj.setState(State.OBJ_STATE_IDLE);
			} else {
				phyComp.setVelocity(ctrlerDir, Configuration.PLAYER_SPEED);
			}
		}
		
		if (phyComp.getSpeed() == 0) {
			return new PkmnPhyStateIdle(phyComp);
		}
		
		// Start a new cell-by-cell movement
		while(residueDist > 0) { // Each iteration move the Entity one cell and resolve the collisions
			int prevPos = (phyComp.getCardinalMovingDir().isAlongX() ? obj.getX() : obj.getY());
			phyComp.moveOneCell(world, residueDist);
			phyComp.resolveCollision(world);
			int currPos = (phyComp.getCardinalMovingDir().isAlongX() ? obj.getX() : obj.getY());
			int dPxls = Math.abs(prevPos - currPos);
			if (dPxls == 0) // Collision detected
				return null;
			residueDist -= dPxls;
		}
		
		if (phyComp.isCrossingCells()) {
			obj.setState(State.OBJ_STATE_MOVING);
		} else {
			obj.setState(State.OBJ_STATE_IDLE);
		}
		
		return null;
	}

}
