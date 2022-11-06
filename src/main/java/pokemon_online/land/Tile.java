/**
 * 
 */
package pokemon_online.land;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cecchi
 *
 */
public class Tile {

	private final String name;
	
	private final List<TileImage> imgs; // Tile frames
	
	public Tile(String name) {
		this.name = name;
		
		imgs = new ArrayList<>();
	}

	public String getName() {
		return name;
	}
	
	public TileImage getImage(int frameCount) {
//		if (imgs.size() > 1) {
//			System.out.println(frameCount);
//		}
		return imgs.get(frameCount % imgs.size());
	}

	public void addImage(File imageFile) {
		TileImage image = new TileImage(imageFile, 0, 0, 32, 32); // FIXME Use image dimensions instead of hard-coded values
		addImage(image);
	}

	public void addImage(TileImage tileImage) {
		imgs.add(tileImage);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
