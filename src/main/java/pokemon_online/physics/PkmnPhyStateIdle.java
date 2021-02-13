/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.utils.GameObjectUtils;

/**
 * @author Cecchi
 *
 */
public class PkmnPhyStateIdle extends PkmnPhyState {

	public PkmnPhyStateIdle(PokemonPhysicsComponent phyComp) {
		super(phyComp);
	}

	@Override
	public void enterState(GameWorld world) {
		// TODO Auto-generated method stub
		phyComp.moveOneCell(world, 0);
	}

	@Override
	public PkmnPhyState updateState(GameObject obj, GameWorld world, long dtMillisec, Direction ctrlerDir) {
		if (ctrlerDir == null) {
			return null;
		}
		if (phyComp.isFrozen()) {
			return null;
		}
		
		if (ctrlerDir == GameObjectUtils.getCardinalFacingDir(obj)) {
			return new PkmnPhyStateMoving(phyComp);
		}
		
		switch(ctrlerDir) {
			case DIR_DOWN:
				obj.setFacingDirection(270);
				break;
			case DIR_LEFT:
				obj.setFacingDirection(180);
				break;
			case DIR_RIGHT:
				obj.setFacingDirection(0);
				break;
			case DIR_UP:
				obj.setFacingDirection(90);
				break;
		}
		
		return new PkmnPhyStateAccelerating(phyComp);
	}

}
