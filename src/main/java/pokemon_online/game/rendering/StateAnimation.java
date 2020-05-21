package pokemon_online.game.rendering;

import java.util.HashMap;
import java.util.Map;

public class StateAnimation {

	private final String stateName;
	
	private final Map<Integer, Animation> directions;
	
	public StateAnimation(String stateName) {
		this.stateName = stateName;
		
		directions = new HashMap<>();
	}
	
	public Animation getAnimation(int directionDegrees) {
		if (!directions.containsKey(directionDegrees)) {
			directions.put(directionDegrees, new Animation());
		}
		return directions.get(directionDegrees);
	}
}
