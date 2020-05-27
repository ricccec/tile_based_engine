/**
 * 
 */
package pokemon_online.game.rendering;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cecchi
 *
 */
public class Animation {
	
	private static final int DEFAULT_FPS = 8;
	
	private final List<String> sprites; // FIXME Use spritesheets
	
	private final int fps;
	
	private final boolean loop;
	
	private final boolean hold;
	
	public Animation(int fps, boolean loop, boolean hold) {
		this.fps = fps;
		this.loop = loop;
		this.hold = hold;
		
		sprites = new ArrayList<>();
	}
	
	public int getFrame(long timeMs) {
		return (int)(timeMs*fps/1000);
	}
	
	public Animation addSprite(String sprite) {
		sprites.add(sprite);
		return this;
	}
	
	public String getSprite(int frame) {
		if (sprites.isEmpty())
			return null;
		return sprites.get(frame % sprites.size());
	}

}
