/**
 * 
 */
package pokemon_online.game;

import pokemon_online.GameObject;

/**
 * @author Cecchi
 *
 */
public class Camera extends GameObject {

	private final int width;
	
	private final int height;
	
	public Camera(int width, int height) {
		this.height = width;
		this.width = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
