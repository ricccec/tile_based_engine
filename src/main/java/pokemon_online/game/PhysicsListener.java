/**
 * 
 */
package pokemon_online.game;

import pokemon_online.GameObject;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public interface PhysicsListener {

	void boundingBoxChanged(GameObject obj, Cell cell);
}
