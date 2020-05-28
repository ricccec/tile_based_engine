/**
 * 
 */
package pokemon_online.game.ia;

import pokemon_online.Component;
import pokemon_online.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class IAComponent extends Component {

	public IAComponent(GameObject obj) {
		super(obj);
	}
	
	public abstract void updateIA(GameWorld world, long dtMillisec);

}
