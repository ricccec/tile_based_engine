/**
 * 
 */
package pokemon_online.land;

/**
 * @author Cecchi
 *
 */
public class Tile {

	private final String name;
	
	private final String img;
	
	public Tile(String name, String img) {
		this.name = name;
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return img;
	}

}
