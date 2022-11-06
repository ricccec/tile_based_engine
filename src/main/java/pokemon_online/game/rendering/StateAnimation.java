package pokemon_online.game.rendering;

import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.rendering.SpriteGraphicsComponent.GraphicsState;


// FIXME Change this class name
public class StateAnimation {
	
	private final GraphicsState state;
	
	private long elapsedMs;
	
	private final Map<Integer, Animation> directions;
	
	public StateAnimation(GraphicsState state) {
		this.state = state;
		
		directions = new HashMap<>();
	}
	
	public void updateAnimation(long dtMillisec) {
		elapsedMs += dtMillisec;
	}
	
	public void setAnimation(int directionDegrees, Animation animation) {
		directions.put(directionDegrees, animation);
	}
	
	public Animation getAnimation(int directionDegrees) {
		// TODO What if there is no animation for the given direction?
		return directions.get(directionDegrees);
	}
	
	public String getCurrentSprinte(int directionDegrees) {
		Animation dirAnimation = getAnimation(directionDegrees);
		if (dirAnimation == null) {
			return null;
		}
		int frameNum = dirAnimation.getFrame(elapsedMs);
		return dirAnimation.getSprite(frameNum);
	}
	

	public void reset() {
		elapsedMs = 0;
	}

	public GraphicsState getState() {
		return state;
	}
}
