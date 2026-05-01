/**
 * 
 */
package pokemon_online.game.interaction.interactions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class InteractionHandler {
	
	public abstract boolean handleInteraction(GameWorld world, GameObject receiver, Interaction msg);
	
}
