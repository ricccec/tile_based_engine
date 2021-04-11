/**
 * 
 */
package pokemon_online.land;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import pokemon_online.ResourcesManager;
import pokemon_online.game.GameObject;
import pokemon_online.game.rendering.SpriteData;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
import pokemon_online.land.ZoneBuilder.ZoneJsonField;
import pokemon_online.land.ZoneBuilder.ZoneType;

/**
 * @author Cecchi
 *
 */
public class ObjectsBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(ObjectsBuilder.class);
	
	enum ObjectType {
		
		TREE("tree");
		
		private static final Map<String, ObjectType> KEY_2_OBJ;
		static {
			KEY_2_OBJ = new HashMap<>();
			for (ObjectType zoneType : ObjectType.values()) {
				KEY_2_OBJ.put(zoneType.key, zoneType);
			}
		}
		
		public static ObjectType parseString(String str) {
			return KEY_2_OBJ.get(str);
		}
		
		public final String key;
		
		private ObjectType(String key) {
			this.key = key;
		}
	}
	
	public enum ObjectJsonField {
		
		OBJ_TYPE("type"),
		OBJ_ROW("row"),
		OBJ_COLUMN("col"),
		OBJ_SPRITE("sprite");
		
		public final String key;
		
		private ObjectJsonField(String key) {
			this.key = key;
		}
	}

	public GameObject buidObject(JSONObject objJSON) {
		ObjectType type = ObjectType.parseString(objJSON.get(ObjectJsonField.OBJ_TYPE.key).toString());
		switch(type) {
		case TREE:
			return buildTreeObject(objJSON);
		default:
			return null;
		}
	}

	private GameObject buildTreeObject(JSONObject objJSON) {
		GameObject result = new GameObject();
		
		// Add sprite data
		try {
			String spriteData = objJSON.get(ObjectJsonField.OBJ_SPRITE.key).toString();
			addGraphicsData(result, spriteData);
		} catch (IOException | ParseException e) {
			LOGGER.error(e.toString());
		}
		
		return result;
	}
	
	private void addGraphicsData(GameObject obj, String graphDataName) throws FileNotFoundException, IOException, ParseException {
		assert(obj.getGraphicsComponent() == null);
		
		SpriteGraphicsComponent grapComp = new SpriteGraphicsComponent(obj);
		obj.setGraphicsComponent(grapComp);
		
		SpriteData sprite = ResourcesManager.getMgr().loadGraphicsData(graphDataName);
		sprite.setGraphics(grapComp);
	}

}
