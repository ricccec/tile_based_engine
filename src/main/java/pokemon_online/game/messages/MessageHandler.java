/**
 * 
 */
package pokemon_online.game.messages;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class MessageHandler {
	
	public abstract boolean handleMessage(GameWorld world, GameObject receiver, Message msg);
	
}
