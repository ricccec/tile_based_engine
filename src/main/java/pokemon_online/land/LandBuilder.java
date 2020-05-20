/**
 * 
 */
package pokemon_online.land;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Cecchi
 *
 */
public class LandBuilder {

	public enum JsonField {
		LAND_NAME("name"),
		LAND_TILES("tiles"),
		LAND_HEIGHT("rowsCount"),
		LAND_WIDTH("colsCount"),
		LAND_ROWS("rows"),
		LAND_ROW("row"),
		LAND_COLUMNS("columns"),
		
		TILE_ID("id"),
		TILE_NAME("name"),
		TILE_IMG("img"),
		TILE_TYPE("type"),
		TILE_WALKABLE("walkable"),
		
		TILE_DOOR("door"),
		TILE_TEXT("text"),
		TILE_ENCOUNTER("encounter"),
		TILE_CHECKPOINT("checkpoint"),
		
		CELL_TILE("tile"),
		CELL_ROW("row"),
		CELL_COLUMN("col"),
		CELL_WALKABLE("walkable"),
		CELL_DOOR("door"),
		CELL_TEXT("text"),
		CELL_ENCOUNTER("encounter"),
		CELL_CHECKPOINT("checkpoint"),
		
		DOOR_TARGET("targetLand"),
		DOOR_TARGET_ROW("targetRow"),
		DOOR_TARGET_COL("targetCol"),
		
		TEXT_MSG("text"),
		
		CHECKPOINT_PRICE("price");
		
		public final String key;
		
		private JsonField(String key) {
			this.key = key;
		}
	}
	
	private final Map<Integer, Tile> tileIds;
	
	public LandBuilder() {
		tileIds = new HashMap<>();
	}
	
	public Land buildLand(JSONObject landJSON) {
		
		// Clear data structures
		tileIds.clear();
		
		String name = landJSON.get(JsonField.LAND_NAME.key).toString();
		int rowsCount = ((Long)landJSON.get(JsonField.LAND_HEIGHT.key)).intValue();
		int colsCount = ((Long)landJSON.get(JsonField.LAND_WIDTH.key)).intValue();
		
		Land land = new Land(name, rowsCount, colsCount);
		
		// Read tiles
		JSONArray tilesJSON = (JSONArray)landJSON.get(JsonField.LAND_TILES.key);
		for (Object obj : tilesJSON) {
			JSONObject tileJSON = (JSONObject)obj;
			Tile tile = buildTile(tileJSON);
			tileIds.put(((Long)tileJSON.get(JsonField.TILE_ID.key)).intValue(), tile);
		}
		// Add tiles to land, in order
		for (int tileId = 0; tileId < tileIds.size(); tileId++) {
			land.addTile(tileIds.get(tileId));
		}
		
		// Read grid
		JSONArray rowsJSON = (JSONArray)landJSON.get(JsonField.LAND_ROWS.key);
		for (Object objRow : rowsJSON) {
			// Read row
			JSONObject rowJSON = (JSONObject)objRow;
			
			JSONArray colsJSON = (JSONArray)rowJSON.get(JsonField.LAND_COLUMNS.key);
			for (Object objCol : colsJSON) {
				// Read cell
				JSONObject colJSON = (JSONObject)objCol;
				int rowIndx = ((Long)colJSON.get(JsonField.CELL_ROW.key)).intValue();
				int colIndx = ((Long)colJSON.get(JsonField.CELL_COLUMN.key)).intValue();
				
				// Read tile
				if (colJSON.containsKey(JsonField.CELL_TILE.key)) {
					int tileIndx = ((Long)colJSON.get(JsonField.CELL_TILE.key)).intValue();
					land.setCellTile(rowIndx, colIndx, tileIndx);
				}
				
				// Read walkable
				if (colJSON.containsKey(JsonField.CELL_WALKABLE.key)) {
					boolean walkable = (boolean)colJSON.get(JsonField.CELL_WALKABLE.key);
					land.setCellWalkable(rowIndx, colIndx, walkable);
				}
				
				// Read door
				Object doorJSON = colJSON.get(JsonField.CELL_DOOR.key);
				if ((doorJSON != null) && (((JSONObject)doorJSON).get(JsonField.DOOR_TARGET.key) != null)) {
					Door door = buildDoor((JSONObject)doorJSON);
					land.setCellDoor(rowIndx, colIndx, door);
				}
			}
		}
		
		return land;
	}
	
	private Tile buildTile(JSONObject tileJSON) {
		String name =  tileJSON.get(JsonField.TILE_ID.key).toString();
		String img =  tileJSON.get(JsonField.TILE_IMG.key).toString();
		return new Tile(name, img);
	}
	
	private Door buildDoor(JSONObject doorJSON) {
		String destName = doorJSON.get(JsonField.DOOR_TARGET.key).toString();
		int destRow = ((Long)doorJSON.get(JsonField.DOOR_TARGET_ROW.key)).intValue();
		int destCol = ((Long)doorJSON.get(JsonField.DOOR_TARGET_COL.key)).intValue();
		return new Door(destName, destRow, destCol);
	}
}
