package pokemon_online.game.rendering;

import java.util.HashMap;
import java.util.Map;

public class StateAnimation {
	
	private final String stateName;
	
	private long elapsedMs;
	
	private final Map<Integer, Animation> directions;
	
	public StateAnimation(String stateName) {
		this.stateName = stateName;
		
		directions = new HashMap<>();
	}
	
	public void updateAnimation(long dtMillisec) {
		elapsedMs += dtMillisec;
	}
	
	public Animation getAnimation(int directionDegrees) {
		if (!directions.containsKey(directionDegrees)) {
			directions.put(directionDegrees, new Animation());
		}
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

	public String getStateName() {
		return stateName;
	}
}
