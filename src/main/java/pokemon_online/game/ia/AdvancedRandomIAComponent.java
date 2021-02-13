/**
 * 
 */
package pokemon_online.game.ia;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.physics.PkmnPhyState;

/**
 * @author Cecchi
 *
 */
public class AdvancedRandomIAComponent extends IAComponent {

	// FIXME Move these to a centralized place? Or make them instance attributes
	public static final float PROB_START_MOVING = 1f;//0.005f;
	public static final float PROB_CHANGE_DIR_WHEN_STOPPED = 0.01f;
	public static final float PROB_CHANGE_DIR_WHEN_MOVING = 0.125f;
	public static final float PROB_STOP_MOVING = 0;//0.01f;
	
	private RandomIAState state;
	
	public AdvancedRandomIAComponent(GameObject obj) {
		super(obj);
		
		state = new RandomIAStateIdle();
	}


	@Override
	public void updateIA(GameWorld world, long dtMillisec) {
		RandomIAState newState = state.updateState(obj);
		if (newState != null) {
			// State has changed
			state = newState;
			state.enterState();
		}
	}
}
