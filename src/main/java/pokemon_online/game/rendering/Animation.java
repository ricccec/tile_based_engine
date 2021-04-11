/**
 * 
 */
package pokemon_online.game.rendering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pokemon_online.land.CroppedImage;

/**
 * @author Cecchi
 *
 */
public class Animation {
	
	private static final int DEFAULT_FPS = 8;
	
	private final List<CroppedImage> sprites; // FIXME Use spritesheets
	
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
		sprites.add(new CroppedImage(new File(sprite), 0, 0, 32, 32)); // FIXME
		return this;
	}
	
	public String getSprite(int frame) {
		if (sprites.isEmpty()) {
			return null;
		}
		
		if (frame < sprites.size()) {
			return sprites.get(frame);
		} else if (loop) {
			return sprites.get(frame % sprites.size());
		} else if (hold) {
			return sprites.get(sprites.size() - 1);
		} else {
			return null;
		}
	}

}
