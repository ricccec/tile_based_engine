/**
 * 
 */
package pokemon_online.game.event;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class EventHandler {
	
	public abstract boolean handleEvent(GameWorld world, GameObject receiver, Event msg);
	
}
