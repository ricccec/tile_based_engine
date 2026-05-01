/**
 * 
 */
package pokemon_online.land;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pokemon_online.game.GameObject;
import pokemon_online.game.interaction.InteractionComponent;
import pokemon_online.game.interaction.interactions.InteractionHandler;
import pokemon_online.game.interaction.interactions.TextInteractionHandler;

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
		LAND_TEXTS("texts"),
		
		TILE_ID("id"),
		TILE_NAME("name"),
		TILE_IMG("img"),
		TILE_TYPE("type"),
		TILE_WALKABLE("walkable"),
		
		TILE_IMG_FRAME_NUMBER("frame"),
		TILE_IMG_FRAME_IMG("img"),
		
		TILE_IMG_TILESET("tilesheet"),
		TILE_IMG_TILESET_X("x"),
		TILE_IMG_TILESET_Y("y"),
		TILE_IMG_TILESET_WIDTH("width"),
		TILE_IMG_TILESET_HEIGHT("height"),
		TILE_IMG_TILESET_SCALE("scale"),
		
		TILE_DOOR("door"), //FIXME OBS?
		TILE_TEXT("text"), //FIXME OBS?
		TILE_ENCOUNTER("encounter"), //FIXME OBS?
		TILE_CHECKPOINT("checkpoint"), //FIXME OBS?
		
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
		
		TEXT_ROW("row"),
		TEXT_COL("col"),
		TEXT_MSG("text"),
		
		CHECKPOINT_PRICE("price");
		
		public final String key;
		
		private JsonField(String key) {
			this.key = key;
		}
	}
	
	private static final Logger LOGGER = Logger.getLogger(LandBuilder.class);
	
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
		
		// Read texts game objects
		JSONArray textsJSON = (JSONArray)landJSON.get(JsonField.LAND_TEXTS.key);
		for (Object obj : textsJSON) {
			// Get text object data
			JSONObject textJSON = (JSONObject)obj;
			String textMsg = textJSON.get(JsonField.TEXT_MSG.key).toString();
			int initRow = ((Long)textJSON.get(JsonField.TEXT_ROW.key)).intValue();
			int initCol = ((Long)textJSON.get(JsonField.TEXT_COL.key)).intValue();
			
			GameObject text = new GameObject();
			
			InteractionComponent intrComp = new InteractionComponent(text);
			intrComp.addInteractionHandler(new TextInteractionHandler(textMsg));
			text.setInteractionComponent(intrComp);
			
			land.addObject(text, initRow, initCol);
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
		String name =  tileJSON.get(JsonField.TILE_NAME.key).toString();
		
		Tile result = new Tile(name);
		parseTileImage(result, tileJSON.get(JsonField.TILE_IMG.key));

		return result;
	}
	
	private void parseTileImage(Tile tile, Object imageData) {
		
		if (imageData instanceof String) {
			tile.addImage(new File(imageData.toString()));
			return;
		}
		if (imageData instanceof JSONObject) {
			TileImage tileImage = parseTileSet((JSONObject)imageData);
			tile.addImage(tileImage);
			return;
		}
		if (imageData instanceof JSONArray) {
			JSONArray frames = (JSONArray)imageData;
			for (Object frame : frames) {
				JSONObject frameJSON = (JSONObject)frame;
				
				int frameCount = ((Long)frameJSON.get(JsonField.TILE_IMG_FRAME_NUMBER.key)).intValue();
				LOGGER.info("Parsing frame " + frameCount + " of tile " + tile);
				
				Object frameImgData = frameJSON.get(JsonField.TILE_IMG_FRAME_IMG.key);
				if (frameImgData instanceof String) {
					tile.addImage(new File(frameImgData.toString()));
				} else if (frameImgData instanceof JSONObject) {
					TileImage frameTileSet = parseTileSet((JSONObject)frameImgData);
					tile.addImage(frameTileSet);
				} else {
					throw new IllegalStateException("Frame " + frameCount + " of tile " + tile + " contains invalid data");
				}
			}
		}
		
	}

	private TileImage parseTileSet(JSONObject imageData) {
		String tileSet = imageData.get(JsonField.TILE_IMG_TILESET.key).toString();
		int x = ((Long)imageData.get(JsonField.TILE_IMG_TILESET_X.key)).intValue();
		int y = ((Long)imageData.get(JsonField.TILE_IMG_TILESET_Y.key)).intValue();
		int width = ((Long)imageData.get(JsonField.TILE_IMG_TILESET_WIDTH.key)).intValue();
		int height = ((Long)imageData.get(JsonField.TILE_IMG_TILESET_HEIGHT.key)).intValue();
		float scale = ((Double)imageData.get(JsonField.TILE_IMG_TILESET_SCALE.key)).floatValue();
		
		return new TileImage(new File(tileSet), x, y, width, height, scale);
	}

	private Door buildDoor(JSONObject doorJSON) {
		String destName = doorJSON.get(JsonField.DOOR_TARGET.key).toString();
		int destRow = ((Long)doorJSON.get(JsonField.DOOR_TARGET_ROW.key)).intValue();
		int destCol = ((Long)doorJSON.get(JsonField.DOOR_TARGET_COL.key)).intValue();
		return new Door(destName, destRow, destCol);
	}
}
