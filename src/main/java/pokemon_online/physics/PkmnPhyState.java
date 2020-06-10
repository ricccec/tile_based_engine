/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class PkmnPhyState {

	protected final PokemonPhysicsComponent phyComp;
	
	public PkmnPhyState(PokemonPhysicsComponent phyComp) {
		this.phyComp = phyComp;
	}
	
	public abstract void enterState(GameWorld world);
	
	public abstract PkmnPhyState updateState(GameObject obj, GameWorld world, long dtMillisec, Direction ctrlerDir);
	
}
