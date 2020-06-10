/**
 * 
 */
package pokemon_online.game;

import pokemon_online.Component;
import pokemon_online.GameObject;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public abstract class PhysicsComponent extends Component {

	private GameObjectsContainer objContainer;
	
	public PhysicsComponent(GameObject obj) {
		super(obj);
	}
	
	public void addListener(GameObjectsContainer container) {
		objContainer = container;
	}
	
	protected void notifyBoundingBoxChanged(Cell cell) {
		if (objContainer != null) {
			objContainer.boundingBoxChanged(obj, cell);
		}
	}
	
	public abstract void update(GameWorld world, long dtMillisec);

	public void removeListener() {
		objContainer = null;
	}

}
