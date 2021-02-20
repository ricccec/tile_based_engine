package pokemon_online.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.event.Event;
import pokemon_online.game.event.EventHandler;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.interaction.InteractionComponent;
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
	
	public enum State {
		ACTIVE,
		FROZEN;
	}

	private static final Logger LOGGER = Logger.getLogger(GameObject.class);
	
	public static final Event EVT_QUEUE_END = new Event(null);
	
	// FIXME Don't use the Observer pattern, make the GameWorld (or GameObjectsContainer) listen to its own objects
	private final Collection<GameObjectListener> listeners;
	
	private final Deque<Event> pendingEvents;
	
	private final Stack<Event> pendingMsgs;
	
	private final Collection<EventHandler> msgHandlers;
	
	private State state;
	
	private int x;
	
	private int y;
	
	protected GraphicsComponent grapComp;
	
	protected InteractionComponent interComp;
	
	private final Stack<PhysicsComponent> physComps;
	
	protected IAComponent iaComp;
	
	protected final Controller ctrl;
	
	/**
	 * The direction the object is facing. Doesn't have to match the moving direction;
	 */
	protected double direction;
	
	public GameObject() {
		state = State.ACTIVE;
		
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
	
	public InteractionComponent getInteractionComponent() {
		return interComp;
	}
	
	public void setInteractionComponent(InteractionComponent interComp) {
		this.interComp = interComp;
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

	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * @param msg
	 */
	public void notifyEvent(GameWorld world, Event msg) {
		// Pass event to handlers (if any) to process it during the current frame
		LOGGER.debug("Object " + this + " has received a message");
		for (EventHandler handler : msgHandlers) {
			handler.handleEvent(world, this, msg);
		}
		pendingMsgs.push(msg); // Put the message into the queue so it can be processed during the next frame
	}
	
	public Deque<Event> getPendingEventsQueue() {
		return pendingEvents;
	}
	
	public void addEventHandler(EventHandler handler) {
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

}
