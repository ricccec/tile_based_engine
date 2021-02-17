package pokemon_online.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.messages.Message;
import pokemon_online.game.messages.MessageHandler;
import pokemon_online.game.rendering.GraphicsComponent;
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
	
	public static final Message EVT_QUEUE_END = new Message(null);
	
	// FIXME Don't use the Observer pattern, make the GameWorld (or GameObjectsContainer) listen to its own objects
	private final Collection<GameObjectListener> listeners;
	
	private final Deque<Message> pendingEvents;
	
	private final Stack<Message> pendingMsgs;
	
	private final Collection<MessageHandler> msgHandlers;
	
	private int x;
	
	private int y;
	
	protected GraphicsComponent grapComp;
	
	private final Stack<PhysicsComponent> physComps;
	
	protected IAComponent iaComp;
	
	protected final Controller ctrl;
	
	/**
	 * The direction the object is facing. Doesn't have to match the moving direction;
	 */
	protected double direction;
	
	public GameObject() {
		ctrl = new Controller();
		
		listeners = new ArrayList<>();
		msgHandlers = new ArrayList<>();
		pendingMsgs = new Stack<>();
		
		pendingEvents = new ArrayDeque<>();
		pendingEvents.add(EVT_QUEUE_END);
		
		physComps = new Stack<>();
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
		if (physComps.isEmpty()) {
			return null;
		} else {
			return physComps.peek();
		}
	}

	public void setPhysicsComponent(PhysicsComponent physComp) {
		physComps.clear();
		physComps.push(physComp);
	}
	
	public void pushPhysicsComponent(PhysicsComponent physComp) {
		physComps.push(physComp);
	}
	
	public void popPhysicsComponent(PhysicsComponent physComp) {
		physComps.pop();
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

	/**
	 * @param msg
	 * @return <code>true</code> if the message has been delivered (not necessarily handled)
	 */
	public boolean sendMessage(GameWorld world, Message msg) {
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
	
	public Deque<Message> getPendingEventsQueue() {
		return pendingEvents;
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
	
	public Iterable<GameObjectListener> getListeners() {
		return listeners;
	}
	
	public void setFrozen(boolean b) {
		if (!physComps.isEmpty()) {
			getPhysicsComponent().setFrozen(b);
		}
		// TODO Freeze all components
	}

}
