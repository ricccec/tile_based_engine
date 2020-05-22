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
public class Controller {
	
	private final Map<Control, Boolean> ctrlsState;
	
	private final Set<Control> stateChanged;
	
	private final Map<Control, Boolean> newEvents;

	public Controller() {
		ctrlsState = new HashMap<>();
		stateChanged = new HashSet<>();
		newEvents = new HashMap<>();
	}
	
	public void setActivated(Control control) {
		newEvents.put(control, true);
	}
	
	public void setDeactivated(Control control) {
		newEvents.put(control, false);
	}
	
	public void updateController() {
		stateChanged.clear();
		
		for (Entry<Control, Boolean> event : newEvents.entrySet()) {
			Control control = event.getKey();
			boolean newState = event.getValue();
			if (isActive(control) != newState) {
				stateChanged.add(control);
			}
			ctrlsState.put(control, newState);
		}
	}
	
	public boolean isStatusChanged(Control control) {
		return stateChanged.contains(control);
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
