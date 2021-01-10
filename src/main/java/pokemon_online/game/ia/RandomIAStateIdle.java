/**
 * 
 */
package pokemon_online.game.ia;

import pokemon_online.game.Controller;
import pokemon_online.game.GameObject;
import pokemon_online.game.Controller.Control;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.Direction;

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
		Controller ctrl = obj.getController();
		ctrl.setDeactivated(Control.MOVE_RIGHT);
		ctrl.setDeactivated(Control.MOVE_DWN);
		ctrl.setDeactivated(Control.MOVE_LEFT);
		ctrl.setDeactivated(Control.MOVE_UP);
		
		// Change direction?
		if (GameUtils.eventOccur(AdvancedRandomIAComponent.PROB_CHANGE_DIR_WHEN_STOPPED)) {
			double currDirDegree = obj.getFacingDirection();
			double newDirDegree = (currDirDegree + (1 + rand.nextInt(3))*90) % 360;
			
			Direction newDirection = Direction.degree2direction(newDirDegree);
			switch(newDirection)  {
			case DIR_DOWN:
				ctrl.setActivated(Control.MOVE_DWN);
				break;
			case DIR_LEFT:
				ctrl.setActivated(Control.MOVE_LEFT);
				break;
			case DIR_RIGHT:
				ctrl.setActivated(Control.MOVE_RIGHT);
				break;
			case DIR_UP:
				ctrl.setActivated(Control.MOVE_UP);
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
