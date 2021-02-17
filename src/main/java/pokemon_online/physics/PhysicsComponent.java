/**
 * 
 */
package pokemon_online.physics;

import org.apache.log4j.Logger;

import pokemon_online.game.Component;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectListener;
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

	protected int speedX; // In pxl/tick

	protected int speedY; // In pxl/tick
	
	private boolean frozen;
	
	public PhysicsComponent(GameObject obj) {
		super(obj);
	}
	
	protected void notifyBoundingBoxChanged(Cell cell) {
		for(GameObjectListener listener : obj.getListeners()) {
			listener.boundingBoxChanged(obj, cell);
		}
	}
	
	public abstract void update(GameWorld world, long dtMillisec);
	
	public abstract Cell getBoundingBox();
	
	public void setFrozen(boolean b) {
		frozen = b;
		LOGGER.debug("Object " + this + (frozen ? " freezed" : " unfreezed"));
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	/**
	 * @return the moving direction, in degrees
	 */
	public double getMovingDirection() {
		double angRad = Math.atan2(-speedY, speedX);
		return GameUtils.radiant2degree(angRad);
	}
	
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public boolean isMoving() {
		return ((getSpeedX() != 0) || (getSpeedY() != 0));
	}

}
