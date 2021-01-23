/**
 * 
 */
package pokemon_online.hud;

import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;

/**
 * @author Cecchi
 *
 */
public class HudText { // FIXME Better HudMessage?

	private final String text;
	
	public HudText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public boolean handleInput(Controller controller) {
		if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1))
			return true;
		else 
			return false;
	}
	
}
