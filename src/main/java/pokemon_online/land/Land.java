/**
 * 
 */
package pokemon_online.land;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.utils.Tuple;

/**
 * @author Cecchi
 *
 */
public class Land {

    private final String name;

    private final int rowsCount;
    private final int colsCount;
    
    private final List<Tile> tileset;
    
    private final LandCell[][] grid;
    
    private final Map<GameObject, Cell> objsInitPosition;
    
    public Land(String name, int rowsCount, int colsCount) {
    	this.name = name;
    	this.rowsCount = rowsCount;
    	this.colsCount = colsCount;
    	
    	tileset = new ArrayList<>();
    	objsInitPosition = new HashMap<>();
    	
    	// Init grid with empty cells
    	grid = new LandCell[rowsCount][colsCount];
    	for (int row = 0; row < rowsCount; row++) {
    		for (int col = 0; col < colsCount; col++) {
    			grid[row][col] = new LandCell();
    		}
    	}
    }
    
    public Tile getTile(int index) {
    	if (index < tileset.size()) {
    		return tileset.get(index);
    	} else {
    		return null;
    	}
    }
    
    public String getName() {
		return name;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public int getColsCount() {
		return colsCount;
	}

	public Tile getCellTile(int row, int col) {
    	if ((row < 0) || (row >= rowsCount))
    		return null;
    	if ((col < 0) || (col >= colsCount))
    		return null;
    	return grid[row][col].getTile();
    }
    
	
	public boolean isWalkable(int row, int col) {
		if ((row < 0) || (row >= rowsCount))
    		return Configuration.EMPTY_CELLS_WALKABLE;
    	if ((col < 0) || (col >= colsCount))
    		return Configuration.EMPTY_CELLS_WALKABLE;
    	return grid[row][col].walkable;
	}
	
    void addTile(Tile tile) {
    	tileset.add(tile);
    }
    
    void setCellTile(int row, int col, int tileIndex) {
    	grid[row][col].tile = tileset.get(tileIndex);
    }
    
    void setCellWalkable(int row, int col, boolean walkable) {
    	grid[row][col].walkable = walkable;
    }
    
	void setCellDoor(int row, int col, Door door) {
		grid[row][col].door = door;
	}
    
	public void addObject(GameObject obj, int initRow, int initCol) {
		objsInitPosition.put(obj, new Cell(initRow, initCol));
	}
	
	public Collection<GameObject> getObjects() {
		return objsInitPosition.keySet();
	}
	
	public Cell getInitialPosition(GameObject obj) {
		return objsInitPosition.get(obj);
	}
	
    static private class LandCell {

    	Tile tile;
    	
    	boolean walkable;
    	
    	Door door;
    	
    	public Tile getTile() {
    		return tile;
    	}
    }
    
}
