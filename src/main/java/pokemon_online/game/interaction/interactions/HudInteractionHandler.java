/**
 * 
 */
package pokemon_online.game.interaction.interactions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameObject.State;

/**
 * @author Cecchi
 *
 */
public class HudInteractionHandler extends InteractionHandler {

	@Override
	public boolean handleInteraction(GameWorld world, GameObject receiver, Interaction evt) {
		switch(evt.getType()) {
			case HUD_DISPOSED:
				receiver.setState(State.ACTIVE);
				return true;
			default:
				return false;
		}
	}


}
