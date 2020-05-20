/**
 * 
 */
package pokemon_online.game;

import pokemon_online.Component;
import pokemon_online.GameObject;

/**
 * @author Cecchi
 *
 */
public abstract class PhysicsComponent extends Component {

	public PhysicsComponent(GameObject obj) {
		super(obj);
	}
	
	public abstract void update(GameWorld world, long dtMillisec);

}
