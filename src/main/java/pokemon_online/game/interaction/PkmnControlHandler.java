/**
 * 
 */
package pokemon_online.game.interaction;

import pokemon_online.game.Controller.Control;
import pokemon_online.game.interaction.event.Event;

import java.util.Collection;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public class PkmnControlHandler extends ControlHandler {

	private static final PkmnControlHandler INSTANCE = new PkmnControlHandler();
	
	public static InteractionComponent getInteractionComponent(GameObject obj) {
		InteractionComponent result = new InteractionComponent(obj);
		result.addControlHandler(Control.ACTION_1, INSTANCE);
		result.addControlHandler(Control.ACTION_2, INSTANCE);
		
		return result;
	}
	
	@Override
	public boolean handleControl(GameWorld world, GameObject controlled, Control cntrl) {
		
		// Get the objects the action has been performed onto
		Collection<GameObject> objects = getActionTargets(world, controlled);
		if (objects.isEmpty()) {
			return false;
		}
		GameObject target = objects.iterator().next();
				
		switch(cntrl) {
		case ACTION_1:
			target.notifyEvent(world, Event.newActionPerformed(controlled));
			return true;
		case ACTION_2:
			target.notifyEvent(world, Event.newActionBPerformed(controlled));
			return true;
		default:
			return false;
		}
	}

}
