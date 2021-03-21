/**
 * 
 */
package pokemon_online.land;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pokemon_online.game.GameObject;

/**
 * @author Cecchi
 *
 */
public class ObjectsBuilder {
	
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
		// TODO Auto-generated method stub
		return null;
	}

}
