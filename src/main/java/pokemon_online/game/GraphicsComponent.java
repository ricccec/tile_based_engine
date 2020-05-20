/**
 * 
 */
package pokemon_online.game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.Component;
import pokemon_online.GameObject;
import pokemon_online.ResourcesManager;
import pokemon_online.game.rendering.Animation;
import pokemon_online.game.rendering.Viewport;

/**
 * @author Cecchi
 *
 */
public class GraphicsComponent extends Component { // TODO Make abstract
	
	private final Map<String, Animation> animations;
	
	public GraphicsComponent(GameObject obj) {
		super(obj);
		animations = new HashMap<>();
		
		animations.put("IDLE", new Animation());
		animations.get("IDLE").addSprite("F Allenatricel_S_Stop.gif");
	}
	
	public void render(Graphics2D grap, Viewport viewport) {
		int scrX = viewport.getScreenX() + obj.getX();
		int scrY = viewport.getScreenY() + obj.getY();
		grap.fillOval(scrX, scrY, 32, 32); // Player is alwais at the center of the screen
		
		String imgName = animations.get("IDLE").getSprite(0);
		Image tileImg = ResourcesManager.getMgr().getTileImage(imgName);
		grap.drawImage(tileImg,scrX, scrY, null);
	}

	public void updateAnimation(long dt) {
		// TODO Auto-generated method stub
		
	}

}
