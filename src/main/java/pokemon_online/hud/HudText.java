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

	// TODO Make this inherit from an abstract class
	
	private final String text;
	
	private boolean disposed;
	
	public HudText(String text) {
		this.text = text;
		
		disposed = false;
	}

	public String getText() {
		return text;
	}

	public void handleInput(Controller controller) {
		if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1))
			disposed = true;
	}
	
	public boolean isDisposed() {
		return disposed;
	}
	
	
	
}
