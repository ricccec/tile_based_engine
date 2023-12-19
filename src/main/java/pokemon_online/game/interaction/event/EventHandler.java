/**
 * 
 */
package pokemon_online.game.interaction.event;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class EventHandler {
	
	/**
	 * Return <code>true<code> if the event has been handled and doesn't need to be added to the object's queue of pending events
	 * @param world
	 * @param receiver
	 * @param evt
	 * @return
	 */
	public abstract boolean handleEvent(GameWorld world, GameObject receiver, Event evt);
	
}
