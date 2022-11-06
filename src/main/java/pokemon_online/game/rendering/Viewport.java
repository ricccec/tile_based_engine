/**
 * 
 */
package pokemon_online.game.rendering;

/**
 * @author Cecchi
 *
 */
public class Viewport {
	
	private final int minRow;
	
	private final int maxRow;
	
	private final int minCol;
	
	private final int maxCol;
	
	private final int screenX;
	
	private final int screenY;
	
	public Viewport(int minRow, int maxRow, int minCol, int maxCol, int screenX, int screenY) {
		this.minRow = minRow;
		this.maxRow = maxRow;
		this.minCol = minCol;
		this.maxCol = maxCol;
		this.screenX = screenX;
		this.screenY = screenY;
	}

	public int getMinRow() {
		return minRow;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public int getMinCol() {
		return minCol;
	}

	public int getMaxCol() {
		return maxCol;
	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

}
