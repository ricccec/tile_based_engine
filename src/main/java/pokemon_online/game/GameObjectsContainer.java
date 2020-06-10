/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pokemon_online.GameObject;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public class GameObjectsContainer implements GameObjectListener, PhysicsListener {
	
	private final static boolean ADD_ZONE_OBJECTS = false;
	
	private final Set<GameObject> objects;
	
	private final Map<Cell, Set<GameObject>> cell2objs;
	
	private final Map<GameObject, Cell> bBoxes;
	
	private final Map<Cell, Collection<GameObject>> cell2bBoxes;
	
	private final Map<GameObject, GameObject> zoneGraphics;
	
	private final GameWorld world;
	
	public GameObjectsContainer(GameWorld world) {
		this.world = world;
		
		objects = new HashSet<>();
		cell2objs = new HashMap<>();
		
		bBoxes = new HashMap<>();
		cell2bBoxes = new HashMap<>();
		
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
	
	public Collection<GameObject> getProps(int row, int col) {
		Cell cell = new Cell(row, col);
		if (cell2bBoxes.containsKey(cell)) {
			return cell2bBoxes.get(cell);
		} else {
			return new ArrayList<>();
		}
	}

	public void clear() {
		for (GameObject object : getObjects()) {
			object.removeListener(this);
			
			// Remove physics listener
			PhysicsComponent pyComp = object.getPhysicsComponent();
			if (pyComp != null) {
				pyComp.removeListener();
			}
		}
		objects.clear();
		cell2objs.clear();
	}
	
	public void addObject(GameObject obj) {
		if (!objects.contains(obj)) {
			objects.add(obj);
			
			obj.addListener(this);
			
			// Add physics listener
			PhysicsComponent pyComp = obj.getPhysicsComponent();
			if (pyComp != null) {
				pyComp.addListener(this);
				
				// FIXME
				if (ADD_ZONE_OBJECTS) {
					GameObject bBoxGraphics = new ZoneDebugObject(Color.RED);
					zoneGraphics.put(obj, bBoxGraphics);
					objects.add(bBoxGraphics);
				}
			}
		}
	}
	
	public Collection<GameObject> getObjects() {
		return objects;
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
		int prevRow = world.getRow(prevY);
		int prevCol = world.getColumn(prevX);
		int currRow = world.getRow(currY);
		int currCol = world.getColumn(currX);
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
		Cell prevCell = bBoxes.get(obj);
		if ((prevCell != null) && (prevCell.equals(cell))) {
			// Nothing changed
			return;
		}
		
		if (prevCell != null) {
			// Remove obsolete data
			assert(cell2bBoxes.containsKey(prevCell));
			cell2bBoxes.get(prevCell).remove(obj);
		}
		
		// Update data
		bBoxes.put(obj, cell);
		if (!cell2bBoxes.containsKey(cell)) {
			cell2bBoxes.put(cell, new HashSet<>());
		}
		cell2bBoxes.get(cell).add(obj);
		
		if (zoneGraphics.containsKey(obj)) {
			GameObject bBoxGraph = zoneGraphics.get(obj);
			bBoxGraph.setPosition(world.getX(cell.getColumn()), world.getY(cell.getRow()));
		}
		
	}
}
