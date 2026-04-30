/**
 * 
 */
package pokemon_online.game.interaction.actions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class ActionHandler {
	
	public abstract boolean handleAction(GameWorld world, GameObject receiver, Action msg);
	
}
