/**
 * 
 */
package pokemon_online.game.interaction;

import pokemon_online.game.Controller.Control;
import pokemon_online.game.interaction.event.Event;

import java.util.Collection;

import org.apache.log4j.Logger;

import pokemon_online.game.Game;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public class PkmnControlHandler extends ControlHandler {

	private static final Logger LOGGER = Logger.getLogger(PkmnControlHandler.class);
			
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
		
		LOGGER.debug("Object " + this + " sending event @T" + Game.getInstance().getGameStats().getCurrTickCount());
		
		for (GameObject target : objects) {
			switch(cntrl) {
				case ACTION_1:
					target.notifyEvent(world, Event.newActionPerformed(controlled));
					break;
				case ACTION_2:
					target.notifyEvent(world, Event.newActionBPerformed(controlled));
					break;
				default:
					return false;
			}
		}
		
		return true;
	}

}
