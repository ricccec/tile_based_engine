/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.Configuration;
import pokemon_online.GameObject;
import pokemon_online.game.GameWorld;

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
	public PkmnPhyState updateState(GameObject obj, GameWorld world, long dtMillisec, Direction ctrlerDir) {
		// FIXME dtMillisec is ignored
		
		int residueDist = Configuration.PLAYER_SPEED;//phyComp.getSpeed();
		if (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0)) {
			assert(phyComp.getSpeed() > 0);
			// A movement from the previous tick is still ongoing
			// Complete the movement
			int prevPos = (phyComp.getMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			phyComp.moveOneCell(world, residueDist);
			phyComp.resolveCollision(world);
			int currPos = (phyComp.getMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			residueDist -= Math.abs(prevPos - currPos);
		}
		
		// The state of the object's controller gets read only when the object reach the next cell
		if (((obj.getX() % 32) == 0) && ((obj.getY() % 32) == 0))  {
			if (ctrlerDir == null) {
				obj.setSpeedX(0);
				obj.setSpeedY(0);
			} else {
				phyComp.setVelocity(ctrlerDir, Configuration.PLAYER_SPEED);
			}
		}
		
		if ((phyComp.getSpeed() == 0))
			return new PkmnPhyStateIdle(phyComp);
		
		// Start a new cell-by-cell movement
		while(residueDist > 0) {
			int prevPos = (phyComp.getMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			phyComp.moveOneCell(world, residueDist);
			phyComp.resolveCollision(world);
			int currPos = (phyComp.getMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			int dPxls = Math.abs(prevPos - currPos);
			if (dPxls == 0) // Block
				return null;
			residueDist -= dPxls;
		}
		
		return null;
	}

}
