/**
 * 
 */
package pokemon_online.game.rendering;

import java.awt.Graphics2D;

import pokemon_online.Component;
import pokemon_online.GameObject;

/**
 * @author Cecchi
 *
 */
public abstract class GraphicsComponent extends Component { // TODO Make abstract
	
	public GraphicsComponent(GameObject obj) {
		super(obj);
	}
	
	public abstract void render(Graphics2D grap, Viewport viewport);

	public abstract void updateAnimation(long dt);
	
}
