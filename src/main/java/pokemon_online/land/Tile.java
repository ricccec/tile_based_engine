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
	
	private final List<CroppedImage> imgs;
	
	public Tile(String name) {
		this.name = name;
		
		imgs = new ArrayList<>();
	}

	public String getName() {
		return name;
	}
	
	public CroppedImage getImage(int frameCount) {
//		if (imgs.size() > 1) {
//			System.out.println(frameCount);
//		}
		return imgs.get(frameCount % imgs.size());
	}

	public void addImage(File imageFile) {
		CroppedImage image = new CroppedImage(imageFile, 0, 0, 32, 32); // FIXME Use image dimensions instead of hard-coded values
		addImage(image);
	}

	public void addImage(CroppedImage tileImage) {
		imgs.add(tileImage);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
