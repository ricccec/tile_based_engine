/**
 * 
 */
package pokemon_online.physics;

import org.apache.commons.math3.analysis.function.Atan2;
import org.apache.log4j.Logger;

import pokemon_online.game.Component;
import pokemon_online.game.Game;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectsContainer;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameUtils;

/**
 * @author Cecchi
 *
 */
public abstract class PhysicsComponent extends Component {
	

	private static final Logger LOGGER = Logger.getLogger(PhysicsComponent.class);

	private GameObjectsContainer objContainer;
	
	private boolean frozen;
	
	public PhysicsComponent(GameObject obj) {
		super(obj);
	}
	
	public void addListener(GameObjectsContainer container) { // FIXME Use setContainer instead?
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
		LOGGER.debug("Object " + this + (frozen ? " freezed" : " unfreezed"));
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void lookToward(int x, int y) {
		int xDiff = x - obj.getX();
		int yDiff = y - obj.getY(); // TODO Move vector operations to a dedicated class?
		
		double angleDiffRad = Math.atan2(-yDiff, xDiff);
		double angleDiffDeg = GameUtils.radiant2degree(angleDiffRad);
		
		obj.setFacingDirection((int)angleDiffDeg);
	}

}
