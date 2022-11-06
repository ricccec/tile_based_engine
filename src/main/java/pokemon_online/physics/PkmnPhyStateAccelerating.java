/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.utils.GameObjectUtils;

/**
 * @author Cecchi
 *
 */
public class PkmnPhyStateAccelerating extends PkmnPhyState {

	long timerMillisec;
	
	public PkmnPhyStateAccelerating(PokemonPhysicsComponent phyComp) {
		super(phyComp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enterState(GameWorld world) {
		timerMillisec = 0;
	}

	@Override
	public PkmnPhyState updateState(GameObject obj, GameWorld world, long dtMillisec, CardinalDirection ctrlerDir) {
		if (ctrlerDir == null) {
			// Controller released, go back to idle
			return new PkmnPhyStateIdle(phyComp);
		}
		if (obj.getState() != State.ACTIVE) {
			// Something has frozen the Entity, go back to idle
			return new PkmnPhyStateIdle(phyComp);
		}
		
		if (GameObjectUtils.getCardinalFacingDir(obj) != ctrlerDir) {
			// Controller changed, reset timer
			timerMillisec = 0;
		}
		
		timerMillisec += dtMillisec;
		if (timerMillisec >= 2*Configuration.MS_PER_UPDATE) { // FIXME Make this a constant
			return new PkmnPhyStateMoving(phyComp);
		} else {
			return null;
		}
	}

}
