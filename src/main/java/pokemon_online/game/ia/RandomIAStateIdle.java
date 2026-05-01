/**
 * 
 */
package pokemon_online.game.ia;

import pokemon_online.game.GameActionsState;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameActionsState.GameAction;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.CardinalDirection;

/**
 * @author Cecchi
 *
 */
public class RandomIAStateIdle extends RandomIAState {

	@Override
	public void enterState() {
		// TODO Auto-generated method stub

	}

	@Override
	public RandomIAState updateState(GameObject obj) {
		
		// Reset controller state
		GameActionsState ctrl = obj.getController();
		ctrl.setDeactivated(GameAction.MOVE_RIGHT);
		ctrl.setDeactivated(GameAction.MOVE_DWN);
		ctrl.setDeactivated(GameAction.MOVE_LEFT);
		ctrl.setDeactivated(GameAction.MOVE_UP);
		
		// Change direction?
		if (GameUtils.eventOccur(AdvancedRandomIAComponent.PROB_CHANGE_DIR_WHEN_STOPPED)) {
			double currDirDegree = obj.getFacingDirection();
			double newDirDegree = (currDirDegree + (1 + rand.nextInt(3))*90) % 360;
			
			CardinalDirection newDirection = CardinalDirection.degree2direction(newDirDegree);
			switch(newDirection)  {
			case DIR_DOWN:
				ctrl.setActivated(GameAction.MOVE_DWN);
				break;
			case DIR_LEFT:
				ctrl.setActivated(GameAction.MOVE_LEFT);
				break;
			case DIR_RIGHT:
				ctrl.setActivated(GameAction.MOVE_RIGHT);
				break;
			case DIR_UP:
				ctrl.setActivated(GameAction.MOVE_UP);
				break;
			default:
				break;
			
			}
		}
		
		// Start moving
		if (GameUtils.eventOccur(AdvancedRandomIAComponent.PROB_START_MOVING)) {
			return new RandomIAStateMoving();
		} else {
			return this;
		}
		
	}

}
