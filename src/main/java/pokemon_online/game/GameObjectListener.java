/**
 * 
 */
package pokemon_online.game;

import pokemon_online.GameObject;

/**
 * @author Cecchi
 *
 */
public interface GameObjectListener {

	void positionChanged(GameObject obj, int prevX, int prevY, int currX, int currY);
}
