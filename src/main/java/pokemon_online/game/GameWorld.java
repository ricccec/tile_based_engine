/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pokemon_online.Configuration;
import pokemon_online.ResourcesManager;
import pokemon_online.game.event.Event;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;
import pokemon_online.land.Land;
import pokemon_online.land.Tile;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class GameWorld {

	private static final Logger LOGGER = Logger.getLogger(GameWorld.class);
	
	private static final boolean DRAW_B_BOX = true;
	
	private final Game game;
	
	private final GameObjectsContainer objContainer;
	
	private final Map<Event.Type, Set<GameObject>> msgListeners;

	private Land currLand;
	
	private long worldTimeMs;

	public GameWorld(Game game) {
		this.game = game;
		objContainer = new GameObjectsContainer(this);
		
		msgListeners = new HashMap<>();
	}

	public void jumpToLand(Land land) {
		this.currLand = land;
		
		// Spawn land objects
		objContainer.clear();
		for (GameObject obj : land.getObjects()) {
			Cell objInitPos = land.getInitialPosition(obj);
			spanObject(obj, objInitPos.getRow(), objInitPos.getColumn());
		}
	}

	public void sendMessage(Event msg) { // FIXME Use "event" instead?
		 game.queueMessage(msg);
	}
	
	// FIXME sendMessage and sendMessageToObjects 
	public void sendMessageToObjects(Event msg) {
		if (msgListeners.containsKey(msg.getType())) {
			for (GameObject listener : msgListeners.get(msg.getType())) {
				listener.notifyEvent(this, msg);
			}
		}
	}
	
	public void spanObject(GameObject obj, int row, int col) {
		obj.setPosition(32*col, 32*row); // FIXME Remove all hard-coded shit
		objContainer.addObject(obj);
		
		LOGGER.debug("Object " + obj + " spawned at (" + row + ", " + col + ")");
	}
	
	public Collection<GameObject> getAllObjects() {
		return objContainer.getAllObjects();
	}
	
	public void beforeUpdate() {
		for (GameObject obj : getAllObjects()) { // FIXME Only active objects
			
			// Prepare the objects's event queue
			Deque<Event> evtQueue = obj.getPendingEventsQueue();
			
			// Remove events generated at frame i-2
			Event currEvent = null;
			while(!evtQueue.isEmpty()) {
				currEvent = evtQueue.pollFirst();
				if (currEvent == GameObject.EVT_QUEUE_END) {
					break;
				}
			}
			assert(currEvent == GameObject.EVT_QUEUE_END); // Event queue contains at least the EVT_QUEUE_END event
			
			// Move  the EVT_QUEUE_END event after the events generated at frame i-1
			evtQueue.addLast(currEvent);

		}
	}
	
	public void updateIA(long dtMillisec) {
		for (GameObject obj : getAllObjects()) { // FIXME Only active objects
			IAComponent iaComp = obj.getIAComponent();
			if (iaComp != null) {
				iaComp.updateIA(this, dtMillisec);
			}
		}
	}
	
	public void updateWorld(long dtMillisec) {
		// Update physics
		for (GameObject obj : getAllObjects()) {
			PhysicsComponent phyComp = obj.getPhysicsComponent();
			if (phyComp != null) {
				phyComp.update(this, dtMillisec);
			}
		}
		
		// Update current time
		worldTimeMs += dtMillisec;
	}
	
	public void updateAnimation(long dtMillisec) {
		for (GameObject obj : getAllObjects()) {
			// FIXME Only update active objects
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.updateAnimation(dtMillisec);
			}
		}
	}

	public void updateControllers() {
		for (GameObject obj : getAllObjects()) {
			// FIXME Only update active objects
			Controller controller = obj.getController();
			if (controller != null) {
				controller.updateController();
			}
		}
	}

	public void renderWorld(Camera camera, Rectangle bounds, Graphics2D grap) {
		
		int componentWidth = bounds.width;
		int componentHeight = bounds.height;
		
		// Cover previous frame
		grap.setColor(Color.BLACK);
		grap.fillRect(0, 0, 1000, 1000);

		if (currLand == null)
			return;

		// Compute camera center coords in screen space
		int plyrScreenX = componentWidth / 2 - 16;
		int plyrScreenY = componentHeight / 2 - 16;

		// Calculate the camera's bounding box in world's space
		// FIXME Camera is always centered at the player: make the camera movable
		int xMin = camera.getX() - (int) (32 * Math.floor((camera.getWidth() / 2) / 32f));
		int xMax = xMin + camera.getWidth();
		int yMin = camera.getY() - (int) (32 * Math.floor((camera.getHeight() / 2) / 32f));
		int yMax = yMin + camera.getHeight();

		// System.out.println("Screen rect: (" + xMin + "," + yMin + "),(" + xMax + ","
		// + yMax + ")"); // FIXME use a fucking logger;

		// Bleed the box to whole cells
		int colMin = xMin / 32; // FIXME no hardcoded shit
		int colMax = (xMax - 1) / 32;
		int rowMin = yMin / 32;
		int rowMax = (yMax - 1) / 32;

		// System.out.println("Screen rect (bleed): (" + colMin + "," + rowMin + "),(" +
		// colMax + "," + rowMax + ")"); // FIXME use a fucking logger;

		// Land origin in screen space
		int landOriginX = plyrScreenX - camera.getX();
		int landOriginY = plyrScreenY - camera.getY();

		// Draw tile's ground sprites
		int currTileFrame = (int)Math.floor((worldTimeMs/1000f)*Configuration.TILE_ANIMATION_SPEED);
//		System.out.println(worldTimeMs + " ms -> " + currTileFrame);
		for (int c = colMin; c <= colMax; c++) {// Per ogni colonna
			for (int r = rowMin; r <= rowMax; r++) {// Per ogni righa

				// Cell coordinates in screen space
				int screenX = landOriginX + 32 * c;
				int screeny = landOriginY + 32 * r;

				Tile tile = currLand.getCellTile(r, c);
				if (tile != null) {
					Image tileImg = ResourcesManager.getMgr().getTileImage(tile.getImage(currTileFrame));
					grap.drawImage(tileImg, screenX, screeny, null);
				}
			}
		}

		// Draw viewport rectangle
		grap.setColor(Color.RED);
		grap.drawRect(componentWidth / 2 - 16 - 64, componentHeight / 2 - 16 - 64, 160, 160);

		// Draw game objects
		Viewport viewport = new Viewport(rowMin, rowMax, colMin, colMax, landOriginX, landOriginY);
		for (GameObject obj : objContainer.getSortedObjectsInRange(rowMin - 1, colMin - 1, rowMax, colMax)) {
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.render(grap, viewport);
			}
		}
	}

	public boolean isWalkable(int row, int col) {
		return currLand.isWalkable(row, col);
	}
	
	public static class Cell { // FIXME move outside this class

		private final int row;
		
		private final int col;
		
		public Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return col;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + col;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (col != other.col)
				return false;
			if (row != other.row)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "[r:" + row + " c:" + col + "]";
		}
	}

	public Collection<GameObject> getProps(int row, int col) {
		return objContainer.getProps(row, col);
	}

	public Collection<GameObject> getObjects(Cell cell) {
		return objContainer.getObjects(cell.row, cell.col);
	}
	
	public Collection<GameObject> getObjects(int row, int col) {
		return objContainer.getObjects(row, col);
	}

	public void addMessageListener(Event.Type msgType, GameObject listener) {
		if (!msgListeners.containsKey(msgType)) {
			msgListeners.put(msgType, new HashSet<>());
		}
		msgListeners.get(msgType).add(listener);
	}
	
	public void removeMessageListener(Event.Type msgType, GameObject listener) {
		if (msgListeners.containsKey(msgType)) {
			msgListeners.get(msgType).remove(listener);
		}
	}

}
