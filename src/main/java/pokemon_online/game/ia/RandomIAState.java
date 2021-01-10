/**
 * 
 */
package pokemon_online.game.ia;

import java.util.Random;

import pokemon_online.game.GameObject;

/**
 * @author Cecchi
 *
 */
public abstract class RandomIAState {
	
	protected final Random rand; // TODO Use a centralized randomizer?
	
	public RandomIAState() {
		rand = new Random();
	}
	
	public abstract void enterState();

	public abstract RandomIAState updateState(GameObject obj);
	
	protected void getCurrentDirection(GameObject obj) {
		
	}
}
