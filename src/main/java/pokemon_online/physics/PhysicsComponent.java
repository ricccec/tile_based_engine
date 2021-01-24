/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.game.Component;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectsContainer;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public abstract class PhysicsComponent extends Component {

	private GameObjectsContainer objContainer;
	
	private boolean frozen;
	
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
	
	public void setFrozen(boolean b) {
		frozen = b;
	}
	
	public boolean isFrozen() {
		return frozen;
	}

}
