/**
 * 
 */
package pokemon_online.hud;

import pokemon_online.hud.HudMenu.HudMenuEventHandler;
import pokemon_online.hud.HudText.HudTextEventHandler;

/**
 * @author Cecchi
 *
 */
public class HudDialog extends HudStateStack implements HudTextEventHandler {
	
	private final HudText msg;
	
	private final HudMenu opts;
	
	public HudDialog(String message, String... options) {
		msg = new HudText(message);
		opts = new HudMenu(options);
		
		msg.setEventHandler(this);
		pushState(msg);
	}

	@Override
	public void onTextDisplayed(HudStateStack stateStack) {
		stateStack.pushState(opts);
	}
	
	public void setEventHandler(HudMenuEventHandler handler) {
		opts.setEventHandler(handler);
	}

}
