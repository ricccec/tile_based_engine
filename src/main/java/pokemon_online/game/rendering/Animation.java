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
	
	private static final int FPS = 8;
	
	private final List<String> sprites; // FIXME Use spritesheets
	
	public Animation() {
		sprites = new ArrayList<>();
	}
	
	public int getFrame(long timeMs) {
		return (int)(timeMs*FPS/1000);
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
