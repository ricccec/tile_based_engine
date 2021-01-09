/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public interface PhysicsListener {

	void boundingBoxChanged(GameObject obj, Cell cell);
}
