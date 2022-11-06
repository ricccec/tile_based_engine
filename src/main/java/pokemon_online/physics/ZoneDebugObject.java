/**
 * 
 */
package pokemon_online.physics;

import java.awt.Color;
import java.awt.Graphics2D;

import pokemon_online.game.GameObject;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;

/**
 * @author Cecchi
 *
 */
public class ZoneDebugObject extends GameObject {

	public ZoneDebugObject(Color color) {
		grapComp = new GraphicsComponent(this) {
			
			@Override
			public void updateAnimation(long dt) {
			}
			
			@Override
			public void render(Graphics2D grap, Viewport viewport) {
				int scrX = viewport.getScreenX() + obj.getX();
				int scrY = viewport.getScreenY() + obj.getY();
				
				grap.setColor(color);
				grap.fillRect(scrX, scrY, 32, 32);
			}
		};
	}
	
}
