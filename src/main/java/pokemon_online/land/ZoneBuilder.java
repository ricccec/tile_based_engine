/**
 * 
 */
package pokemon_online.land;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.land.zone.JumpZone;
import pokemon_online.land.zone.TriggerZone;
import pokemon_online.land.zone.obstacle.PlatformObstacle;
import pokemon_online.physics.CardinalDirection;

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
	
	public Collection<GameObject> buidZone(JSONObject zoneJson) {
		Collection<GameObject>  results = new ArrayList<>();
		ZoneType type = ZoneType.parseString(zoneJson.get(ZoneJsonField.ZONE_TYPE.key).toString());
		switch(type) {
			case PLATFORM:
				results.addAll(buildPlatformZone(zoneJson));
				break;
			case JUMP:
				results.addAll(buildJumpZone(zoneJson));
				break;
			default:
				break;
		}
		return results;
	}
	
	public Cell getInitialPosition(GameObject zoneObject) {
		return initPositions.get(zoneObject);
	}

	private Collection<GameObject> buildPlatformZone(JSONObject zoneJson) {
		
		Collection<GameObject>  results = new ArrayList<>();
		
		Cell initPos = parseInitialPosizio(zoneJson);
		
		PlatformObstacle obj = new PlatformObstacle(initPos);
		
		JSONArray blockedDirs = (JSONArray)zoneJson.get(ZoneJsonField.ZONE_PLATFORM_BLOCKED_DIR.key);
		for (Object blockedDir : blockedDirs) {
			obj.addBlockedDirection(CARDINAL_DIRS.get(blockedDir.toString()));
		}
		
		
		initPositions.put(obj, initPos);
		results.add(obj);
		
		return results;
	}
	
	private Collection<GameObject> buildJumpZone(JSONObject zoneJson) {
		
		Collection<GameObject>  results = new ArrayList<>();
		
		// Get jump direction
		String dirStr = zoneJson.get(ZoneJsonField.ZONE_JUMP_DIR.key).toString();
		CardinalDirection dir = CARDINAL_DIRS.get(dirStr);
		if (dir == CardinalDirection.DIR_UP) {
			throw new IllegalArgumentException();
		}
		
		// Create jump zone
		JumpZone obj = new JumpZone(dir);
		Cell initPos = parseInitialPosizio(zoneJson);
		initPositions.put(obj, initPos);
		results.add(obj);
		
		// Create obstacle to prevent "climbing"
		PlatformObstacle block = null;
		switch (dir) {
			case DIR_DOWN:
				block = new PlatformObstacle(initPos.withRow(initPos.getRow() - 1));
				block.addBlockedDirection(CardinalDirection.DIR_UP);
				break;
			case DIR_LEFT:
				block = new PlatformObstacle(initPos);
				block.addBlockedDirection(CardinalDirection.DIR_UP);
				block.addBlockedDirection(CardinalDirection.DIR_DOWN);
				block.addBlockedDirection(CardinalDirection.DIR_RIGHT);
				break;
			case DIR_RIGHT:
				block = new PlatformObstacle(initPos);
				block.addBlockedDirection(CardinalDirection.DIR_UP);
				block.addBlockedDirection(CardinalDirection.DIR_DOWN);
				block.addBlockedDirection(CardinalDirection.DIR_LEFT);
				break;
			default:
				break;
		}
		if (block != null) {
			initPositions.put(block, GameUtils.getCell(block.getX(), block.getY()));
			results.add(block);
		}
		
		// Create triggered obstacles to prevent strange behaviors
		if ((dir == CardinalDirection.DIR_LEFT) || (dir == CardinalDirection.DIR_RIGHT)) {
			
			Cell jumpingCell = initPos;
			Cell landingCell = initPos.withColumn(initPos.getColumn() + ((dir == CardinalDirection.DIR_LEFT) ? -1 : 1));
			
			// Add triggered block zone for the landing cell
			TriggerZone landingTrigger = new TriggerZone();
			initPositions.put(landingTrigger, landingCell);
			results.add(landingTrigger);
			GameObject landingTarget = landingTrigger.buildTarget(jumpingCell);
			initPositions.put(landingTarget, jumpingCell);
			results.add(landingTarget);
			
			// Add triggered block zone for the jumping cell
			TriggerZone jumpingTrigger = new TriggerZone();
			initPositions.put(jumpingTrigger, jumpingCell);
			results.add(jumpingTrigger);
			GameObject jumpingTarget = jumpingTrigger.buildTarget(landingCell);
			initPositions.put(jumpingTarget, landingCell);
			results.add(jumpingTarget);
		}
		
		return results;
	}
	
	private Cell parseInitialPosizio(JSONObject zoneJson) {
		int row = ((Long)zoneJson.get(ZoneJsonField.ZONE_ROW.key)).intValue();
		int col = ((Long)zoneJson.get(ZoneJsonField.ZONE_COLUMN.key)).intValue();
		return new Cell(row, col);
	}


}
