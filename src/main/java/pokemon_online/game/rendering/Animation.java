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
	
	private static final int FPS = 16;
	
	private final List<String> sprites; // FIXME Use spritesheets
	
	private long stateStart;
	
	public Animation() {
		sprites = new ArrayList<>();
	}
	
	public void reset() {
		stateStart = 0;
	}
	
	public int getCurrentFrame(long dt) {
		long elapsedMs = dt - stateStart;
		return (int) (elapsedMs*FPS/1000);
	}
	
	public void addSprite(String sprite) {
		sprites.add(sprite);
	}
	
	public String getSprite(int frame) {
		if (sprites.isEmpty())
			return null;
		return sprites.get(frame % sprites.size());
	}

}
