/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.PhysicsComponent;
import pokemon_online.physics.ZoneDebugObject;

/**
 * @author Cecchi
 *
 */
public class GameObjectsContainer implements GameObjectListener {
	
	private final static boolean ADD_ZONE_OBJECTS = false;
	
	private final Set<GameObject> objects;
	
	private final Map<Cell, Set<GameObject>> cell2objs;
	
	private final Map<GameObject, Cell> bBoxes;
	
	private final Map<Cell, Collection<GameObject>> cell2Obstacle;
	
	private final Map<Cell, Collection<GameObject>> cell2Zones; // Zones have only an interaction component (no physics, no graphics)
	
	private final Map<GameObject, GameObject> zoneGraphics;
	
	private final GameWorld world; // FIXME Needed?
	
	public GameObjectsContainer(GameWorld world) {
		this.world = world;
		
		objects = new HashSet<>();
		cell2objs = new HashMap<>();
		
		bBoxes = new HashMap<>();
		cell2Obstacle = new HashMap<>();
		cell2Zones = new HashMap<>();
		
		zoneGraphics = new HashMap<>();
	}

	public Collection<GameObject> getObjects(int row, int col) {
		Cell cell = new Cell(row, col);
		if (cell2objs.containsKey(cell)) {
			return cell2objs.get(cell);
		} else {
			return new ArrayList<>();
		}
	}
	
	public List<GameObject> getSortedObjectsInRange(int fromRow, int fromCol, int toRow, int toCol) {
		
		List<GameObject> results = new ArrayList<>();
		assert(fromRow <= toRow);
		assert(fromCol <= toCol);
		for (int r = fromRow; r <= toRow; r++) {
			for (int c = fromCol; c <= toCol; c++) {
				results.addAll(getObjects(r, c));
			}
		}
		
		// Sort objects
		Collections.sort(results, new Comparator<GameObject>() {

			@Override
			public int compare(GameObject o1, GameObject o2) {
				return Integer.compare(o1.getY(), o2.getY());
			}
		});
		
		return results;
		
	}
	
	public Collection<GameObject> getAllObjects() {
		return objects;
	}
	
	public Collection<GameObject> getZones(Cell cell) {
		if (cell2Zones.containsKey(cell)) {
			return cell2Zones.get(cell);
		} else {
			return new ArrayList<>();
		}
	}
	
	public Collection<GameObject> getObstacles(int row, int col) {
		Cell cell = new Cell(row, col);
		if (cell2Obstacle.containsKey(cell)) {
			return cell2Obstacle.get(cell);
		} else {
			return new ArrayList<>();
		}
	}

	public void clear() {
		for (GameObject object : getAllObjects()) {
			object.removeListener(this);
		}
		objects.clear();
		cell2objs.clear();
	}
	
	public void addObject(GameObject obj) {
		if (objects.contains(obj)) {
			throw new IllegalArgumentException("Object " + obj + " is already part of world " + world);
		}
		
		objects.add(obj);
		
		// Update data structures
		int objRow = GameUtils.getRow(obj.getY());
		int objCol = GameUtils.getColumn(obj.getX());
		Cell objCell = new Cell(objRow, objCol);
		if (!cell2objs.containsKey(objCell)) {
			cell2objs.put(objCell, new HashSet<>());
		}
		cell2objs.get(objCell).add(obj);
		
		
		if (GameObjectUtils.isObstacle(obj)) {  // This is a Prop
			// FIXME find a better name for Props
			obj.addListener(this);
			// Add to Props data structure
			boundingBoxChanged(obj, objCell);
		}
		if (GameObjectUtils.isZone(obj)) { // This is a zone
			if (!cell2Zones.containsKey(objCell)) {
				cell2Zones.put(objCell, new HashSet<>());
			}
			cell2Zones.get(objCell).add(obj);
		}
		
//		// FIXME
//		if (ADD_ZONE_OBJECTS) {
//			GameObject bBoxGraphics = null;
//			if (GameObjectUtils.isZone(obj)) {
//				bBoxGraphics = new ZoneDebugObject(new Color(1f, 1f, 0f, .5f));
//			}
//			
//			if (bBoxGraphics != null) {
//				bBoxGraphics.setX(obj.getX());
//				bBoxGraphics.setY(obj.getY());
//				addObject(bBoxGraphics);
//				zoneGraphics.put(obj, bBoxGraphics);
////				objects.add(bBoxGraphics);
//			}
//		}
		
	}

	@Override
	public void positionChanged(GameObject obj, int prevX, int prevY, int currX, int currY) {
		
		if ((prevX == currX) && (prevY == currY)) {
			return;
		}
		
		if (!objects.contains(obj)) {
			// FIXME Might slow down the loop?
			throw new IllegalArgumentException("Object " + obj + " is not registered in this container");
		}
		
//		System.out.println(obj + " (" + prevX + "," + prevY + ") -> (" + currX + ", " + currY + ")");
		// Update object cell
		int prevRow = GameUtils.getRow(prevY);
		int prevCol = GameUtils.getColumn(prevX);
		int currRow = GameUtils.getRow(currY);
		int currCol = GameUtils.getColumn(currX);
		if ((prevRow != currRow) || (prevCol != currCol)) {
			// Cell is actually changed
			Cell prevCell = new Cell(prevRow, prevCol);
			Cell currCell = new Cell(currRow, currCol);
			
			if (cell2objs.containsKey(prevCell)) {
				// Remove obsolete data
				cell2objs.get(prevCell).remove(obj);
				if (cell2objs.get(prevCell).isEmpty()) {
					cell2objs.remove(prevCell);
				}
			}
			
			// Add new data
			if (!cell2objs.containsKey(currCell)) {
				cell2objs.put(currCell, new HashSet<>());
			}
			cell2objs.get(currCell).add(obj);

		}
		
	}

	@Override
	public void boundingBoxChanged(GameObject obj, Cell cell) {
		
		assert(GameObjectUtils.isObstacle(obj));
		
		Cell prevCell = bBoxes.get(obj);
		if ((prevCell != null) && (prevCell.equals(cell))) {
			// Nothing changed
			return;
		}
		
		if (prevCell != null) {
			// Remove obsolete data
			assert(cell2Obstacle.containsKey(prevCell));
			cell2Obstacle.get(prevCell).remove(obj);
		}
		
		// Update data
		bBoxes.put(obj, cell);
		if (!cell2Obstacle.containsKey(cell)) {
			cell2Obstacle.put(cell, new HashSet<>());
		}
		cell2Obstacle.get(cell).add(obj);
		
		if (zoneGraphics.containsKey(obj)) {
			GameObject bBoxGraph = zoneGraphics.get(obj);
			bBoxGraph.setPosition(GameUtils.getX(cell.getColumn()), GameUtils.getY(cell.getRow()));
		}
		
	}
}
