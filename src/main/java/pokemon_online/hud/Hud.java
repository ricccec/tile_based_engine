/**
 * 
 */
package pokemon_online.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ListIterator;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.Controller;
import pokemon_online.game.Game;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.Event.Type;

/**
 * @author Cecchi
 *
 */
public class Hud {
	private static final Logger LOGGER = Logger.getLogger(Hud.class);
	
	private final Stack<HudText> elements;
	
	private final Game game;
	
	public Hud(Game game) {
		this.game = game;
		this.elements = new Stack<>();
	}
	
	public void displayText(String text) {
		elements.push(new HudText(text));
	}
	
	public void renderHud(Graphics2D grap) {
		ListIterator<HudText> itrts = elements.listIterator(elements.size());
		while(itrts.hasPrevious()) {
			HudText currElement = itrts.previous();
//			LOGGER.debug("Rendering HUD element " + currElement);
			grap.setColor(Color.BLUE);
			grap.drawString(currElement.getText(), 32, 32); // FIXME Make the element draw itself
		}
	}

	public void update(Controller controller) {
		if (elements.isEmpty()) {
			return;
		}
		
		HudText activeElement = elements.pop();
		activeElement.handleInput(controller);
		if (!activeElement.isDisposed()) {
			elements.push(activeElement);
		} else if (elements.isEmpty()) {
			game.queueMessage(new Event(Type.HUD_DISPOSED));
		}
		
		
	}
}
