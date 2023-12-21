/**
 * 
 */
package pokemon_online.game.interaction.event;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectState;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public class HudEventHandler extends EventHandler {

	@Override
	public boolean handleEvent(GameWorld world, GameObject receiver, Event evt) {
		
		if (evt.getType() == EventType.HUD_DISPOSED) {
//			System.out.println("Disposed " + receiver);
			receiver.setState(GameObjectState.OBJ_STATE_IDLE);
			return true;
		} else {
			return false;
		}
	}


}
