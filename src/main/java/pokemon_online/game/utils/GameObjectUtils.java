/**
 * 
 */
package pokemon_online.game.utils;

import pokemon_online.game.GameObject;
import pokemon_online.physics.CardinalDirection;

/**
 * @author Cecchi
 *
 */
public class GameObjectUtils {

	public static void lookToward(GameObject obj, int x, int y) {
		int xDiff = x - obj.getX();
		int yDiff = y - obj.getY(); // TODO Move vector operations to a dedicated class?
		
		double angleDiffRad = Math.atan2(-yDiff, xDiff);
		double angleDiffDeg = GameUtils.radiant2degree(angleDiffRad);
		
		obj.setFacingDirection((int)angleDiffDeg);
	}
	
	public static void lookToward(GameObject obj, GameObject otherObj) {
		lookToward(obj, otherObj.getX(), otherObj.getY());
	}

	public static CardinalDirection getCardinalFacingDir(GameObject obj) {
		// FIXME Merge with the previous method
		double movingDir = obj.getFacingDirection();
		return CardinalDirection.degree2direction(movingDir);
	}
	
	
}
