/**
 * 
 */
package pokemon_online.game.rendering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pokemon_online.game.rendering.SpriteGraphicsComponent.GraphicsState;

/**
 * @author Cecchi
 *
 */
public class SpriteData {
	
	public static final String JSON_TYPE = "object graphics";
	
	public enum JsonField {
		TYPE("type"),
		NAME("name"),
		STATES("states"),
		
		STATE_NAME("name"),
		STATE_DIRECTIONS("directions"),
		
		DIR_ANGLE("angle"),
		DIR_ANIMATION("animation"),
		
		ANIMATION_LOOP("loop"),
		ANIMATION_HOLD("hold"),
		ANIMATION_FPS("fps"),
		ANIMATION_FRAMES("frames"),
		
		FRAME_NUM("number"),
		FRAME_SPRITE("sprite");
		
		public final String key;
		
		private JsonField(String key) {
			this.key = key;
		}
	}

	private final String name;
	
	private final JSONObject graphicsJSON;
	
	private final Map<String, Map<Integer, Animation>> stateAnimations;
	
	public SpriteData(JSONObject graphicsJSON) {
		this.graphicsJSON = graphicsJSON;
		
		name = parseName(graphicsJSON);
		stateAnimations = new HashMap<>();
	}
	
	public void setGraphics(SpriteGraphicsComponent gComp) {
		
		for (StateAnimation graphState : parseJSONData()) {
			gComp.addGraphicsState(graphState);
		}
		
	}
	
	private String parseName(JSONObject graphicsJSON) {
		
		String type = graphicsJSON.get(JsonField.TYPE.key).toString();
		if (type.equals(JSON_TYPE)) {
			throw new IllegalArgumentException("JSON is not a game object");
		}
		
		String name = graphicsJSON.get(JsonField.NAME.key).toString();
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<StateAnimation> parseJSONData() {
		String type = graphicsJSON.get(JsonField.TYPE.key).toString();
		if (type.equals(JSON_TYPE)) {
			throw new IllegalArgumentException("JSON is not a game object");
		}
		
		Collection<StateAnimation> results = new ArrayList<>();
		Iterable<JSONObject> stateJSONs = (Iterable<JSONObject>)graphicsJSON.get(JsonField.STATES.key);
		for (JSONObject stateJSON : stateJSONs) {
			results.add(parseJSONState(stateJSON));
		}
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	private StateAnimation parseJSONState(JSONObject jSONState) {
		String name = jSONState.get(JsonField.STATE_NAME.key).toString();
		GraphicsState graphState = GraphicsState.getStateByName(name);
		StateAnimation result = new StateAnimation(graphState);
		
		// Add current state to the states map
		if (!stateAnimations.containsKey(name)) {
			stateAnimations.put(name, new HashMap<>());
		}
		
		// Parse directions
		Iterable<JSONObject> dirJSONs = (Iterable<JSONObject>)jSONState.get(JsonField.STATE_DIRECTIONS.key);
		for (JSONObject dirJSON : dirJSONs) {
			int dirAngle = ((Long)dirJSON.get(JsonField.DIR_ANGLE.key)).intValue();
			if (!stateAnimations.get(name).containsKey(dirAngle)) {
				// Parse animation
				JSONObject animJson = (JSONObject)dirJSON.get(JsonField.DIR_ANIMATION.key);
				Animation stateAnim = parseAnimation(animJson);
				stateAnimations.get(name).put(dirAngle, stateAnim);
			}
			// Animations are stateless objects, hence can be shared among multiple entities
			result.setAnimation(dirAngle, stateAnimations.get(name).get(dirAngle));
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Animation parseAnimation(JSONObject jSONAnim) {
		boolean loop = (boolean)jSONAnim.get(JsonField.ANIMATION_LOOP.key);
		boolean hold = (boolean)jSONAnim.get(JsonField.ANIMATION_HOLD.key);
		int fps = ((Long)jSONAnim.get(JsonField.ANIMATION_FPS.key)).intValue();
		
		Animation result = new Animation(fps, loop, hold);
		
		// Parse frames
		Map<Integer, String> frames = new HashMap<>();
		Iterable<JSONObject> frameJSONs = (Iterable<JSONObject>)jSONAnim.get(JsonField.ANIMATION_FRAMES.key);
		for (JSONObject frameJSON : frameJSONs) {
			int num = ((Long)frameJSON.get(JsonField.FRAME_NUM.key)).intValue();
			String sprite = frameJSON.get(JsonField.FRAME_SPRITE.key).toString();
			frames.put(num, sprite);
		}
		
		// Add frames to animation
		for (int i = 0; i < frames.size(); i++) {
			String sprite = frames.get(i);
			result.addSprite(sprite);
		}
		
		return result;
	}

	public String getGraphicsDataName() {
		return name;
	}
}
