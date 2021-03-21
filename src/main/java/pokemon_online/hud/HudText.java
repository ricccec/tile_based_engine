/**
 * 
 */
package pokemon_online.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import pokemon_online.Configuration;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;

/**
 * @author Cecchi
 *
 */
public class HudText extends HudState {
	
	private final String text;
	
	private  HudTextEventHandler handler;
	
	private float textSpeed; // In letters per seconds;
	
	public HudText(String text) {
		this.text = text;
		
		textSpeed = Configuration.DEFAULT_TEXT_SPEED;
	}

	public String getText() {
		return text;
	}

	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		grap.setColor(Color.BLUE);
		grap.drawString(getText(), 32, 32); // TODO
	}

	@Override
	public void update(long dtMillisec, Controller controller) {
		if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1)) {
			if (handler != null) {
				handler.onTextDisplayed(getStateStack());
			}
			dispose();
		}
	}
	
	public void setEventHandler(HudTextEventHandler handler) {
		this.handler = handler;
	}
	
	public interface HudTextEventHandler{
		public void onTextDisplayed(HudStateStack stateStack);
	}
	
}
