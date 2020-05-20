/**
 * 
 */
package pokemon_online.game;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cecchi
 *
 */
public class Controller {
	
	private final Map<Control, Boolean> ctrlsState;

	public Controller() {
		ctrlsState = new HashMap<>();
	}
	
	public void setActivated(Control control) {
		ctrlsState.put(control, true);
	}
	
	public void setDeactivated(Control control) {
		ctrlsState.put(control, false);
	}
	
	public boolean isActive(Control control) {
		if (ctrlsState.containsKey(control)) {
			return ctrlsState.get(control);
		} else {
			return false;
		}
	}
	
	public boolean allDeactivated(Control... ctrls) {
		for (Control ctrl : ctrls) {
			if (isActive(ctrl)) {
				return false;
			}
		}
		return true;
	}
}
