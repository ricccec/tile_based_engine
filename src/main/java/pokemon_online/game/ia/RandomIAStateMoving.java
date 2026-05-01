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
public class RandomIAStateMoving extends RandomIAState {

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
		
		// Stop moving?
		if (GameUtils.eventOccur(AdvancedRandomIAComponent.PROB_STOP_MOVING)) {
			return new RandomIAStateIdle();
		}
				
		// Change direction?
		double currDirDegree = obj.getFacingDirection();
		if (GameUtils.eventOccur(AdvancedRandomIAComponent.PROB_CHANGE_DIR_WHEN_MOVING)) {
			currDirDegree = (currDirDegree + (1 + rand.nextInt(3))*90) % 360;
		}
		CardinalDirection newDirection = CardinalDirection.degree2direction(currDirDegree);
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
		return this;
		
	}

}
