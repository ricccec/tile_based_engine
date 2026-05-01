/**
 * 
 */
package pokemon_online.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.GameActionsState.GameAction;

/**
 * @author Cecchi
 *
 */
public class Keyboard {

	// TODO Make the mapping key->control configurable
	private static final Map<Integer, GameAction> KEY_MAPPING;
	
	static {
		KEY_MAPPING = new HashMap<>();
		
		KEY_MAPPING.put(37, GameAction.MOVE_LEFT);
		KEY_MAPPING.put(38, GameAction.MOVE_UP);
		KEY_MAPPING.put(39, GameAction.MOVE_RIGHT);
		KEY_MAPPING.put(40, GameAction.MOVE_DWN);
		
		KEY_MAPPING.put(81, GameAction.ACTION_1); // 'Q' key
		KEY_MAPPING.put(87, GameAction.ACTION_2); // 'W' key
	}
	
	private final Collection<GameActionsState> inputs;
	
	public Keyboard() {
		inputs = new ArrayList<>();
	}
	
	public void attachInputState(GameActionsState i) {
		inputs.add(i);
	}
	
	public void detachInputState(GameActionsState i) {
		inputs.remove(i);
	}
	
	public void keyPressed(int keyCode) {
		if (!KEY_MAPPING.containsKey(keyCode))
			return;
		// Propagate event to all controllers
		for (GameActionsState i : inputs) {
			i.setActivated(KEY_MAPPING.get(keyCode));
		}
	}
	
	public void keyReleased(int keyCode) {
		if (!KEY_MAPPING.containsKey(keyCode))
			return;
		// Propagate event to all controllers
		for (GameActionsState i : inputs) {
			i.setDeactivated(KEY_MAPPING.get(keyCode));
		}
	}
}
