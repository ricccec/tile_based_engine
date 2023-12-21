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

	private final HudTextGraphicBuffer grapBuffer; 
	
	private final int optsCount;
	
	public HudMenu(String... options) {
		
		optsCount = options.length;
		
		grapBuffer = new HudTextGraphicBuffer(optsCount, 4, 16, 16); // FIXME VIEW should pass width/height	
		grapBuffer.setWrapTextEnabled(false);
		
		for (String opt : options) {
			for (char c = 0; c < opt.length(); c++) {
				grapBuffer.writeChar(opt.charAt(c));
			}
			grapBuffer.nextRow();
		}
	}
	
	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		grap.drawImage(grapBuffer.getImage(), 32 , 32, null);
	}

	@Override
	public void update(long dtMillisec, Controller controller) {
		// TODO handle the case of menus with many options that needs vertical scrolling
		
		
	}
	
	public void setEventHandler(HudMenuEventHandler handler) {
	}
	
	public interface HudMenuEventHandler {
		public void onSelect(HudStateStack stateStack, int index);
	}

}
