/**
 * 
 */
package pokemon_online.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ListIterator;
import java.util.Stack;

import pokemon_online.game.Controller;

/**
 * @author Cecchi
 *
 */
public class Hud {

	private final Stack<HudText> elements;
	
	public Hud() {
		this.elements = new Stack<>();
	}
	
	public void displayText(String text) {
		elements.push(new HudText(text));
	}
	
	public void renderHud(Graphics2D grap) {
		ListIterator<HudText> itrts = elements.listIterator(elements.size());
		while(itrts.hasPrevious()) {
			HudText currElement = itrts.previous();
			grap.setColor(Color.BLUE);
			grap.drawString(currElement.getText(), 32, 32); // FIXME Make the element draw itself
		}
	}

	public void update(Controller controller) {
		if (elements.isEmpty()) {
			return;
		}
		HudText activeElement = elements.pop();
		if (!activeElement.handleInput(controller)) {
			elements.push(activeElement);
		}
	}
}
