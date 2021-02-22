/**
 * 
 */
package pokemon_online.game.interaction.event;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameObject.State;

/**
 * @author Cecchi
 *
 */
public class HudEventHandler extends EventHandler {

	@Override
	public boolean handleEvent(GameWorld world, GameObject receiver, Event evt) {
		switch(evt.getType()) {
			case HUD_DISPOSED:
				receiver.setState(State.ACTIVE);
				return true;
			default:
				return false;
		}
	}


}
