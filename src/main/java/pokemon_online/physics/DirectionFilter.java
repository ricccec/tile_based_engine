/**
 * 
 */
package pokemon_online.physics;

import static pokemon_online.game.Controller.Control.MOVE_DWN;
import static pokemon_online.game.Controller.Control.MOVE_LEFT;
import static pokemon_online.game.Controller.Control.MOVE_RIGHT;
import static pokemon_online.game.Controller.Control.MOVE_UP;

import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;

/**
 * In case you want a controller with at most one active directional control at
 * a time, this class provide a filtering mechanism.
 * 
 * @author Cecchi
 *
 */
public class DirectionFilter {

	private CardinalDirection controllerDirection;
	
	private final Controller ctrl;
	
	public DirectionFilter(Controller controller) {
		ctrl = controller;
	}
	
	public CardinalDirection getControllerDirection() {
		return controllerDirection;
	}
	
	public void updateControllerDirection() {

		if (ctrl == null) {
			return;
		}
		
		// None active
		if (ctrl.allDeactivated(MOVE_LEFT, MOVE_DWN, MOVE_RIGHT, MOVE_UP)) {
			controllerDirection = null;
			return;
		}
		
		// All but one active
		switch(getHighestPriorityActiveCntrl(ctrl)) {
		case DIR_DOWN:
			if (ctrl.allDeactivated(Control.MOVE_LEFT, Control.MOVE_RIGHT, Control.MOVE_UP)) {
				controllerDirection = CardinalDirection.DIR_DOWN;
				return;
			}
			break;
		case DIR_LEFT:
			if (ctrl.allDeactivated(Control.MOVE_DWN, Control.MOVE_RIGHT, Control.MOVE_UP)) {
				controllerDirection = CardinalDirection.DIR_LEFT;
				return;
			}
			break;
		case DIR_RIGHT:
			if (ctrl.allDeactivated(Control.MOVE_DWN, Control.MOVE_LEFT, Control.MOVE_UP)) {
				controllerDirection = CardinalDirection.DIR_RIGHT;
				return;
			}
			break;
		case DIR_UP:
			if (ctrl.allDeactivated(Control.MOVE_DWN, Control.MOVE_RIGHT, Control.MOVE_LEFT)) {
				controllerDirection = CardinalDirection.DIR_UP;
				return;
			}
			break;
		default:
			break;
		
		}
		
		// Multiple activated, but one just changed
		if (ctrl.isStatusChanged(MOVE_RIGHT)) {
			if (ctrl.isActive(MOVE_RIGHT)) {
				// RIGHT pressed
				controllerDirection = CardinalDirection.DIR_RIGHT;
			} else {
				// RIGHT released
				controllerDirection = getHighestPriorityActiveCntrl(ctrl);
			}
		} else if (ctrl.isStatusChanged(MOVE_DWN)) {
			if (ctrl.isActive(MOVE_DWN)) {
				// DOWN pressed
				controllerDirection = CardinalDirection.DIR_DOWN;
			} else {
				// DOWN released
				controllerDirection = getHighestPriorityActiveCntrl(ctrl);
			}
		} else if (ctrl.isStatusChanged(MOVE_LEFT)) {
			if (ctrl.isActive(MOVE_LEFT)) {
				// LEFT pressed
				controllerDirection = CardinalDirection.DIR_LEFT;
			} else {
				// LEFT released
				controllerDirection = getHighestPriorityActiveCntrl(ctrl);
			}
		} else if (ctrl.isStatusChanged(MOVE_UP)) {
			if (ctrl.isActive(MOVE_UP)) {
				// UP pressed
				controllerDirection = CardinalDirection.DIR_UP;
			} else {
				// UP released
				controllerDirection = getHighestPriorityActiveCntrl(ctrl);
			}
		} 
	}
	
	private CardinalDirection getHighestPriorityActiveCntrl(Controller ctrl) {
		if (ctrl.isActive(MOVE_RIGHT)) {
			return CardinalDirection.DIR_RIGHT;
		}
		if (ctrl.isActive(MOVE_DWN)) {
			return CardinalDirection.DIR_DOWN;
		}
		if (ctrl.isActive(MOVE_LEFT)) {
			return CardinalDirection.DIR_LEFT;
		}
		if (ctrl.isActive(MOVE_UP)) {
			return CardinalDirection.DIR_UP;
		}
		return null;
	}
	
}
