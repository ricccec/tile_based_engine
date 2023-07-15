/**
 * 
 */
package pokemon_online.physics;

import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.Component;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectListener;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.Event.Type;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.utils.Tuple;

/**
 * @author Cecchi
 *
 */
public abstract class PhysicsComponent extends Component {
	
	private static final Logger LOGGER = Logger.getLogger(PhysicsComponent.class);

	protected int speedX; // In pxl/tick

	protected int speedY; // In pxl/tick
	
	private final Stack<Tuple<Integer, Integer>> prevPosition;
	
	private final Stack<Cell> prevBBox;
	
	public PhysicsComponent(GameObject obj) {
		super(obj);
		
		prevPosition = new Stack<>();
		prevBBox = new Stack<>();
	}
	
	protected void notifyBoundingBoxChanged(Cell cell) {
		for(GameObjectListener listener : obj.getListeners()) {
			listener.boundingBoxChanged(obj, cell);
		}
	}
	
	public void beforeUpdate() {
		prevPosition.push(Tuple.newTuple(obj.getX(), obj.getY()));
		prevBBox.push(getBoundingBox());
	}
	
	public abstract void update(GameWorld world, long dtMillisec);
	
	public void afterUpdate() {
		if (prevPosition.size() > 1) {
			if ((obj.getX() == getPrevX()) && (obj.getY() == getPrevY())) {
				// Position hasn't changed
				prevPosition.pop();
			}
		}
		
		if (prevBBox.size() > 1) {
			if (getBoundingBox() == getPrevBoundingBox()) {
				prevBBox.pop();
			}
		}
	}
	
	public void checkZoneInteraction(GameWorld world) {
		Cell prevBBox = getPrevBoundingBox();
		Cell currBBox = getBoundingBox();
		if (!currBBox.equals(prevBBox)) {
			// Notify cells just left
			for (GameObject zone : world.getZones(prevBBox)) {
				assert(GameObjectUtils.isZone(zone));
				zone.getInteractionComponent().notifyEvent(world, Event.newEventWithSender(obj, Type.ZONE_EXITING));
			}
			// Notify cells entering
			for (GameObject zone : world.getZones(currBBox)) {
				assert(GameObjectUtils.isZone(zone));
				zone.getInteractionComponent().notifyEvent(world, Event.newEventWithSender(obj, Type.ZONE_ENTERING));
			}
		}
		
	}
	
	public abstract Cell getBoundingBox(); // TODO Add support for objects that occupy more than one cell
	
	/**
	 * @param otherPhy
	 * @return <code>true</code> in case of collision
	 */
	public boolean checkCollision(PhysicsComponent otherPhy) {
		return GameObjectUtils.testBBoxOverlap(getGameObject(), otherPhy.getGameObject());
	}
	
	public int getPrevX() {
		return prevPosition.peek().getKey();
	}
	
	public int getPrevY() {
		return prevPosition.peek().getValue();
	}
	
	public Cell getPrevBoundingBox() {
		if (prevBBox.isEmpty()) {
			return null;
		}
		return prevBBox.peek();
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
