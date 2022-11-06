/**
 * 
 */
package pokemon_online.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.Controller.Control;

/**
 * @author Cecchi
 *
 */
public class Keyboard {

	// TODO Make the mapping key->control configurable
	private static final Map<Integer, Control> KEY_MAPPING;
	
	static {
		KEY_MAPPING = new HashMap<>();
		
		KEY_MAPPING.put(37, Control.MOVE_LEFT);
		KEY_MAPPING.put(38, Control.MOVE_UP);
		KEY_MAPPING.put(39, Control.MOVE_RIGHT);
		KEY_MAPPING.put(40, Control.MOVE_DWN);
		
		KEY_MAPPING.put(81, Control.ACTION_1); // 'Q' key
		KEY_MAPPING.put(87, Control.ACTION_2); // 'W' key
	}
	
	private final Collection<Controller> controllers;
	
	public Keyboard() {
		controllers = new ArrayList<>();
	}
	
	public void attachController(Controller ctrl) {
		controllers.add(ctrl);
	}
	
	public void detachController(Controller ctrl) {
		controllers.remove(ctrl);
	}
	
	public void keyPressed(int keyCode) {
		if (!KEY_MAPPING.containsKey(keyCode))
			return;
		// Propagate event to all controllers
		for (Controller ctrl : controllers) {
			ctrl.setActivated(KEY_MAPPING.get(keyCode));
		}
	}
	
	public void keyReleased(int keyCode) {
		if (!KEY_MAPPING.containsKey(keyCode))
			return;
		// Propagate event to all controllers
		for (Controller ctrl : controllers) {
			ctrl.setDeactivated(KEY_MAPPING.get(keyCode));
		}
	}
}
