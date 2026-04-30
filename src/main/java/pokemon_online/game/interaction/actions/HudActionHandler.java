/**
 * 
 */
package pokemon_online.game.interaction.actions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameObject.State;

/**
 * @author Cecchi
 *
 */
public class HudActionHandler extends ActionHandler {

	@Override
	public boolean handleAction(GameWorld world, GameObject receiver, Action evt) {
		switch(evt.getType()) {
			case HUD_DISPOSED:
				receiver.setState(State.ACTIVE);
				return true;
			default:
				return false;
		}
	}


}
