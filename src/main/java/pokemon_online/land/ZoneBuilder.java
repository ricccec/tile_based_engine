/**
 * 
 */
package pokemon_online.land;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.physics.CardinalDirection;
import pokemon_online.physics.PlatformPhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class ZoneBuilder {

	enum ZoneType {
		
		PLATFORM("platform"),
		JUMP("jump");
		
		private static final Map<String, ZoneType> KEY_2_ZONE;
		static {
			KEY_2_ZONE = new HashMap<>();
			for (ZoneType zoneType : ZoneType.values()) {
				KEY_2_ZONE.put(zoneType.key, zoneType);
			}
		}
		
		public static ZoneType parseString(String str) {
			return KEY_2_ZONE.get(str);
		}
		
		public final String key;
		
		private ZoneType(String key) {
			this.key = key;
		}
	}
	
	public enum ZoneJsonField {
		
		ZONE_TYPE("type"),
		ZONE_ROW("row"),
		ZONE_COLUMN("col"),
		
		ZONE_PLATFORM_BLOCKED_DIR("blocked"),
		
		ZONE_JUMP_DIR("direction");
		
		public final String key;
		
		private ZoneJsonField(String key) {
			this.key = key;
		}
	}
	
	private static final Map<String, CardinalDirection> CARDINAL_DIRS;
	static {
		CARDINAL_DIRS = new HashMap<>();
		CARDINAL_DIRS.put("up", CardinalDirection.DIR_UP);
		CARDINAL_DIRS.put("dn", CardinalDirection.DIR_DOWN);
		CARDINAL_DIRS.put("lt", CardinalDirection.DIR_LEFT);
		CARDINAL_DIRS.put("rt", CardinalDirection.DIR_RIGHT);
	}
	
	private final Map<GameObject, Cell> initPositions;
	
	public ZoneBuilder() {
		initPositions = new HashMap<>();
	}
	
	public GameObject buidZone(JSONObject zoneJson) {
		ZoneType type = ZoneType.parseString(zoneJson.get(ZoneJsonField.ZONE_TYPE.key).toString());
		if (type == null) return null; // FIXME Remove this
		switch(type) {
			case PLATFORM:
				return buildPlatformZone(zoneJson);
			
			default:
				return null;
		}
	}
	
	public Cell getInitialPosition(GameObject zoneObject) {
		return initPositions.get(zoneObject);
	}

	private GameObject buildPlatformZone(JSONObject zoneJson) {
		
		GameObject result = new GameObject();
		PlatformPhysicsComponent phyComp = new PlatformPhysicsComponent(result);
		result.pushPhysicsComponent(phyComp);
		
		JSONArray blockedDirs = (JSONArray)zoneJson.get(ZoneJsonField.ZONE_PLATFORM_BLOCKED_DIR.key);
		for (Object blockedDir : blockedDirs) {
			phyComp.addBlockedDirection(CARDINAL_DIRS.get(blockedDir.toString()));
		}
		
		Cell initPos = parseInitialPosizio(zoneJson);
		initPositions.put(result, initPos);
		
		return result;
	}
	
	private GameObject buildJumpZone(JSONObject zoneJson) {
		
		GameObject result = new GameObject();
		PlatformPhysicsComponent phyComp = new PlatformPhysicsComponent(result);
		result.pushPhysicsComponent(phyComp);
		
		JSONArray blockedDirs = (JSONArray)zoneJson.get(ZoneJsonField.ZONE_PLATFORM_BLOCKED_DIR.key);
		for (Object blockedDir : blockedDirs) {
			phyComp.addBlockedDirection(CARDINAL_DIRS.get(blockedDir.toString()));
		}
		
		Cell initPos = parseInitialPosizio(zoneJson);
		initPositions.put(result, initPos);
		
		return result;
	}
	
	private Cell parseInitialPosizio(JSONObject zoneJson) {
		int row = ((Long)zoneJson.get(ZoneJsonField.ZONE_ROW.key)).intValue();
		int col = ((Long)zoneJson.get(ZoneJsonField.ZONE_COLUMN.key)).intValue();
		return new Cell(row, col);
	}


}
