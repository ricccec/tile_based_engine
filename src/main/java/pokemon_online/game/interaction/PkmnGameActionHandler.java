/**
 * 
 */
package pokemon_online.game.interaction;

import pokemon_online.game.GameActionsState.GameAction;
import pokemon_online.game.interaction.interactions.Interaction;

import java.util.Collection;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public class PkmnGameActionHandler extends GameActionHandler {

	private static final PkmnGameActionHandler INSTANCE = new PkmnGameActionHandler();
	
	public static InteractionComponent getInteractionComponent(GameObject obj) {
		InteractionComponent result = new InteractionComponent(obj);
		result.addGameActionHandler(GameAction.ACTION_1, INSTANCE);
		result.addGameActionHandler(GameAction.ACTION_2, INSTANCE);
		
		return result;
	}
	
	@Override
	public boolean handleAction(GameWorld world, GameObject controlled, GameAction cntrl) {
		
		// Get the objects the action has been performed onto
		Collection<GameObject> objects = getActionTargets(world, controlled);
		if (objects.isEmpty()) {
			return false;
		}
		GameObject target = objects.iterator().next();
				
		switch(cntrl) {
		case ACTION_1:
			target.notifyEvent(world, Interaction.newActionAPerformed(controlled));
			return true;
		case ACTION_2:
			target.notifyEvent(world, Interaction.newActionBPerformed(controlled));
			return true;
		default:
			return false;
		}
	}

}
