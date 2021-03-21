/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import pokemon_online.game.Controller;

/**
 * @author Cecchi
 *
 */
public class HudStateStack extends HudState {
	
	private static final Logger LOGGER = Logger.getLogger(HudStateStack.class);
	
	private final Deque<HudState> states;
	
	public HudStateStack() {
		states = new ArrayDeque<>();
		
	}
	
	public void pushState(HudState state) {
		assert(!state.isDisposed());
		
		LOGGER.debug("HUD State " + state + " pushed");
		state.setParent(states.peek());
		state.setStateStack(this);
		states.push(state);
		
		disposed = false;
	}
	
	public HudState peekState() {
		return states.peek();
	}
	
	public HudState popState() {
		return states.pop();
	}
	
	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		Iterator<HudState> itrtr = states.descendingIterator();
		while(itrtr.hasNext()) {
			HudState state = itrtr.next();
			state.renderHud(width, height, grap);
		}
	}

	@Override
	public void update(long dtMillisec, Controller controller) {

		
		
		if (!states.isEmpty()) {
			// Update active state (the one on top of HUD)
			HudState state = states.peek();
			state.update(dtMillisec, controller);
		}
		
		// Remove elements disposed 
		while (!states.isEmpty()) {
			HudState state = states.peek();
			if (state.isDisposed()) {
				states.pop();
			} else {
				break;
			}
		}
		
		// Hud disposed?
		disposed = states.isEmpty();
		
	}

}
