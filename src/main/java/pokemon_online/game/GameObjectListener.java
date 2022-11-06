/**
 * 
 */
package pokemon_online.game;

import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public interface GameObjectListener {

	void positionChanged(GameObject obj, int prevX, int prevY, int currX, int currY);
	
	public void boundingBoxChanged(GameObject obj, Cell cell);
	
}
