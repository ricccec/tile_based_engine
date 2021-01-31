package pokemon_online.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.messages.Message;
import pokemon_online.game.messages.MessageHandler;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.PhysicsComponent;

/**
 * 
 */

/**
 * @author Cecchi
 *
 */
public class GameObject {

	private static final Logger LOGGER = Logger.getLogger(GameObject.class);
	
	// FIXME Don't use the Observer pattern, make the GameWorld (or GameObjectsContainer) listen to its own objects
	private final Collection<GameObjectListener> listeners;
	
	private final Stack<Message> pendingMsgs;
	
	private final Collection<MessageHandler> msgHandlers;
	
	private GameWorld world;
	
	private int x;
	
	private int y;
	
	protected GraphicsComponent grapComp;
	
	protected PhysicsComponent physComp;
	
	protected IAComponent iaComp;
	
	protected final Controller ctrl;
	
	protected int speedX; // In pxl/tick

	protected int speedY; // In pxl/tick
	
	/**
	 * The direction the object is facing. Doesn't have to match the moving direction;
	 */
	protected double direction;
	
	public GameObject() {
		ctrl = new Controller();
		
		listeners = new ArrayList<>();
		msgHandlers = new ArrayList<>();
		pendingMsgs = new Stack<>();
	}

	public Controller getCtrl() {
		return ctrl;
	}

	/**
	 * @return the object's X coordinate in the world's space
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the object's Y coordinate in the world's space
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return the moving direction, in degrees
	 */
	public double getMovingDirection() {
		double angRad = Math.atan2(-speedY, speedX);
		return GameUtils.radiant2degree(angRad);
	}
	
	/**
	 * @return the direction the object is facing, in degrees
	 */
	public double getFacingDirection() {
		return direction;
	}
	
	public void setFacingDirection(int dirDegree) {
		direction = dirDegree;
	}
	
	public Controller getController() {
		return ctrl;
	}

	public IAComponent getIAComponent() {
		return iaComp;
	}
	
	public void setIAComponent(IAComponent iaComp) {
		this.iaComp = iaComp;
	}
	
	public GraphicsComponent getGraphicsComponent() {
		return grapComp;
	}
	
	public void setGraphicsComponent(GraphicsComponent grapComp) {
		this.grapComp = grapComp;
	}
	
	public PhysicsComponent getPhysicsComponent() {
		return physComp;
	}

	public void setPhysicsComponent(PhysicsComponent physComp) {
		this.physComp = physComp;
	}

	public void setWorld(GameWorld world) {
		this.world = world;
	}
	
	public void setX(int x) {
		setPosition(x, this.y);
	}
	
	public void setY(int y) {
		setPosition(this.x, y);
	}
	
	public void setPosition(int x, int y) {
		int prevX = this.x;
		int prevY = this.y;
		this.x = x;
		this.y = y;
		
		if ((prevX != x) || (prevY != y)) {
			// Notify listeners the position has changed
			for (GameObjectListener listener : listeners) {
				listener.positionChanged(this, prevX, prevY, x, y);
			}
		}
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
	

	/**
	 * @param msg
	 * @return <code>true</code> if the message has been delivered (not necessarily handled)
	 */
	public boolean sendMessage(Message msg) {
		// Check weather there is a message handler for this message
		LOGGER.debug("Object " + this + " has received a message");
		for (MessageHandler handler : msgHandlers) {
			if (handler.handleMessage(world, this, msg)) {
				return true;
			}
		}
		pendingMsgs.push(msg); // No handler found. Put the message into the queue and hope it will be handled by one of the object's components
		return true;
	}
	
	public void addMessageHandler(MessageHandler handler) {
		msgHandlers.add(handler);
	}

	public void addListener(GameObjectListener listener) {
		listeners.add(listener);
	}

	public void removeListener(GameObjectListener listener) {
		listeners.remove(listener);
	}
	
	public void setFrozen(boolean b) {
		if (physComp != null) {
			physComp.setFrozen(b);
		}
		// TODO Freeze all components
	}

}
