/**
 * 
 */
package pokemon_online.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Cecchi
 *
 */
public class GameActionsState {
	
	public enum GameAction {

		MOVE_UP,
		MOVE_DWN,
		MOVE_LEFT,
		MOVE_RIGHT,
		
		ACTION_1,
		ACTION_2;
		
	}

	private final Map<GameAction, Boolean> actionsState;
	
	private final Set<GameAction> changedActions;
	
	private final Map<GameAction, Boolean> newActions;

	public GameActionsState() {
		actionsState = new HashMap<>();
		changedActions = new HashSet<>();
		newActions = new HashMap<>();
	}
	
	public void setActivated(GameAction action) {
		newActions.put(action, true);
	}
	
	public void setDeactivated(GameAction action) {
		newActions.put(action, false);
	}
	
	public void updateState() {
		changedActions.clear();
		
		for (Entry<GameAction, Boolean> a : newActions.entrySet()) {
			GameAction action = a.getKey();
			boolean newState = a.getValue();
			if (isActive(action) != newState) {
				changedActions.add(action);
			}
			actionsState.put(action, newState);
		}
	}
	
	public boolean isStatusChanged(GameAction action) {
		return changedActions.contains(action);
	}
	
	public boolean isActive(GameAction action) {
		if (actionsState.containsKey(action)) {
			return actionsState.get(action);
		} else {
			return false;
		}
	}
	
	public boolean allDeactivated(GameAction... actions) {
		for (GameAction a : actions) {
			if (isActive(a)) {
				return false;
			}
		}
		return true;
	}
}
