/**
 * 
 */
package pokemon_online.game;

import java.awt.Graphics2D;

/**
 * @author Cecchi
 *
 */
public abstract class GameState {

	public abstract void update(long dtMillisec);
	
	public abstract void render(int width, int height, Graphics2D graph);
}
