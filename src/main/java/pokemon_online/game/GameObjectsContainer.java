/**
 * 
 */
package pokemon_online.game;

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
public class GameObjectsContainer implements GameObjectListener {
	
	private final Set<GameObject> objects;
	
	private final Map<Cell, Set<GameObject>> cell2objs;
	
	private final GameWorld world;
	
	public GameObjectsContainer(GameWorld world) {
		this.world = world;
		
		objects = new HashSet<>();
		cell2objs = new HashMap<>();
	}

	public Collection<GameObject> getObjects(int row, int col) {
		Cell cell = new Cell(row, col);
		if (cell2objs.containsKey(cell)) {
			return cell2objs.get(cell);
		} else {
			return new ArrayList<GameObject>();
		}
	}

	public void clear() {
		for (GameObject object : objects) {
			object.removeListener(this);
		}
		objects.clear();
		cell2objs.clear();
	}
	
	public void addObject(GameObject obj) {
		if (objects.add(obj)) {
			obj.addListener(this);
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
			// FIXME Might slow down thge loop?
			throw new IllegalArgumentException("Object " + obj + " is not registered in this container");
		}
		
		// Update object cell
		int prevRow = world.getRow(prevY);
		int prevCol = world.getColumn(prevX);
		int currRow = world.getRow(currX);
		int currCol = world.getColumn(currY);
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
				
				// Add new data
				if (!cell2objs.containsKey(currCell)) {
					cell2objs.put(currCell, new HashSet<>());
				}
				cell2objs.get(currCell).add(obj);
			}
		}
		
	}
}
