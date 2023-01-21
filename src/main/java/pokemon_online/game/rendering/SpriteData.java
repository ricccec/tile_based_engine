/**
 * 
 */
package pokemon_online.game.rendering;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pokemon_online.game.rendering.SpriteGraphicsComponent.GraphicsState;
import pokemon_online.land.CroppedImage;

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
		FRAME_SPRITE("sprite"),
		FRAME_SCALE_FACTOR("scale"),
		FRAME_CROP("crop"),
		FRAME_ANCHOR("anchor"),
		
		CROP_X("x"),
		CROP_Y("y"),
		CROP_WIDTH("width"),
		CROP_HEIGHT("height"),
		
		ANCHOR_X("x"),
		ANCHOR_Y("y");
		
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
		Map<Integer, CroppedImage> frames = new HashMap<>();
		Iterable<JSONObject> frameJSONs = (Iterable<JSONObject>)jSONAnim.get(JsonField.ANIMATION_FRAMES.key);
		for (JSONObject frameJSON : frameJSONs) {
			int num = ((Long)frameJSON.get(JsonField.FRAME_NUM.key)).intValue();
			CroppedImage frameSprite = parseFrame(frameJSON);
			
			frames.put(num, frameSprite);
		}
		
		// Add frames to animation
		for (int i = 0; i < frames.size(); i++) {
			CroppedImage sprite = frames.get(i);
			result.addSprite(sprite);
		}
		
		return result;
	}
	
	private CroppedImage parseFrame(JSONObject frameJSON) {
		// FIXME {@link LandBuilder#parseTileSet} does the same thing. Turn these into a single method
		
		String spriteFile = frameJSON.get(JsonField.FRAME_SPRITE.key).toString();
		
		// Optional field SCALE FACTOR
		float scaleFactor = CroppedImage.DEFAULT_SCALE_FACTOR;
		if (frameJSON.containsKey(JsonField.FRAME_SCALE_FACTOR.key)) {
			scaleFactor = ((Double)frameJSON.get(JsonField.FRAME_SCALE_FACTOR.key)).floatValue();
		}
		
		// Optional field CROP
		Rectangle crop = CroppedImage.DEFAULT_CROP;
		if (frameJSON.containsKey(JsonField.FRAME_CROP.key)) {
			JSONObject cropJSON =  (JSONObject)frameJSON.get(JsonField.FRAME_CROP.key);
			
			int x = ((Long)cropJSON.get(JsonField.CROP_X.key)).intValue();
			int y = ((Long)cropJSON.get(JsonField.CROP_Y.key)).intValue();
			int width = ((Long)cropJSON.get(JsonField.CROP_WIDTH.key)).intValue();
			int height = ((Long)cropJSON.get(JsonField.CROP_HEIGHT.key)).intValue();
			
			crop = new Rectangle(x, y, width, height);
		}
		
		// Optional field ANCHOR
		Point anchor = CroppedImage.DEFAULT_ANCHOR;
		if (frameJSON.containsKey(JsonField.FRAME_ANCHOR.key)) {
			JSONObject anchorJSON =  (JSONObject)frameJSON.get(JsonField.FRAME_ANCHOR.key);
			
			int x = ((Long)anchorJSON.get(JsonField.ANCHOR_X.key)).intValue();
			int y = ((Long)anchorJSON.get(JsonField.ANCHOR_Y.key)).intValue();
			
			anchor = new Point(x, y);
		}
		
		
		return new CroppedImage(new File(spriteFile), crop, anchor, scaleFactor);
	}

	public String getGraphicsDataName() {
		return name;
	}
}
