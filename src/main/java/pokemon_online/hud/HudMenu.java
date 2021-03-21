/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;

import pokemon_online.game.Controller;
import pokemon_online.hud.HudText.HudTextEventHandler;

/**
 * @author Cecchi
 *
 */
public class HudMenu extends HudState {

	public HudMenu(String... options) {
		
	}
	
	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long dtMillisec, Controller controller) {
		// TODO Auto-generated method stub
		
	}
	
	public void setEventHandler(HudMenuEventHandler handler) {
	}
	
	public interface HudMenuEventHandler {
		public void onSelect(HudStateStack stateStack, int index);
	}

}
