/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

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
	
	private final HudStateStack stack;
	
	private final List<HudState> newStates;
	
	private final Game game;
	
	public Hud(Game game) {
		this.game = game;
		
		stack = new HudStateStack();
		newStates = new ArrayList<>();
	}
	
	public void pushState(HudState state) {
		newStates.add(state);
	}
	
	public void renderHud(int width, int height, Graphics2D grap) {
		stack.renderHud(width, height, grap);
	}

	public void update(long dtMillisec, Controller controller) {

		boolean disposed = stack.isDisposed();
		stack.update(dtMillisec, controller);
		
		/* Needed 'cause one of the state might interact with a game object which in turn might push
		 * a new state into the HUD. This way the state will be pushed but it won't be updated until
		 * the next frame
		 */
		for (HudState newState : newStates) {
			stack.pushState(newState);
		}
		newStates.clear();
		
		if ((!disposed) && stack.isDisposed()) { // Hud just disposed
			game.queueMessage(new Event(Type.HUD_DISPOSED));
		}
		
	}
}
