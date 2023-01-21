package pokemon_online.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.Configuration;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.interaction.InteractionComponent;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.EventHandler;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;
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
	
	public enum State {
		ACTIVE,
		FROZEN;
	}

	private static final Logger LOGGER = Logger.getLogger(GameObject.class);
	
	public static final Event EVT_QUEUE_END = new Event(null);
	
	private static int nextObjectId = 0; // FIXME
	
	// FIXME Don't use the Observer pattern, make the GameWorld (or GameObjectsContainer) listen to its own objects
	private final Collection<GameObjectListener> listeners;
	
	private final Deque<Event> pendingEvents;
	
	private final int id;

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
		
		id = nextObjectId++;
		
		state = State.ACTIVE;
		
		ctrl = new Controller();
		
		listeners = new ArrayList<>();
		
		pendingEvents = new ArrayDeque<>();
		pendingEvents.add(EVT_QUEUE_END);
		
		physComps = new Stack<>();
	}

	public int getId() {
		return id;
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
		LOGGER.debug(physComp + " pushed");
	}
	
	public void popPhysicsComponent() {
		PhysicsComponent phy = physComps.pop();
		LOGGER.debug(phy + " popped");
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
		if (state != this.state) {
//			LOGGER.debug(this + "state: " + this.state + "->" + state);
			this.state = state;
		}
	}
	
	public void renderDebugInfo(Graphics2D grap, Viewport viewport) {
		assert(Configuration.DEBUG);
		
		// Draw bounding box (FIXME this code couples the grapic and phys. components, remove it or use a cleaner solution (e.g. each
		// component renders its own debug information?)
		PhysicsComponent phyComp = getPhysicsComponent();
		if ((phyComp != null) ) {
			Cell bBox = phyComp.getBoundingBox();
			
			int bBoxScrX = viewport.getScreenX() + GameUtils.getX(bBox.getColumn());
			int bBoxScrY = viewport.getScreenY() + GameUtils.getY(bBox.getRow());
			
			grap.setColor(new Color(1f, 0f, 0f, 0.5f));
			grap.fillRect(bBoxScrX, bBoxScrY, Configuration.CELL_SIZE_PXLS, Configuration.CELL_SIZE_PXLS);
		}
					
		// Draw object ID
		grap.setColor(Color.BLUE);
		int scrX = viewport.getScreenX() + getX();
		int scrY = viewport.getScreenY() + getY();
		grap.drawString(Integer.toString(getId()), scrX, scrY);
		
	}
	
	/**
	 * @param evt
	 */
	public void notifyEvent(GameWorld world, Event evt) {
		LOGGER.debug("Object " + this + " has received an event " + evt.getType());
		if (interComp != null) {
			interComp.notifyEvent(world, evt); // Event preprocessing
		}
		pendingEvents.addLast(evt); // Put the message into the queue so it can be processed during the next frame
	}
	
	public Deque<Event> getPendingEventsQueue() {
		return pendingEvents;
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
	
	@Override
	public String toString() {
		return id + "[" + state + "]";
	}

}
