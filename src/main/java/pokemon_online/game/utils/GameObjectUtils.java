/**
 * 
 */
package pokemon_online.game.utils;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.physics.CardinalDirection;
import pokemon_online.physics.PhysicsComponent;

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
	
	// TODO Make a table of theese
	
	public static boolean isEntity(GameObject obj) {
		return ((obj.getGraphicsComponent() != null) &&
				(obj.getInteractionComponent() != null) &&
				(obj.getPhysicsComponent() != null) &&
				(obj.getIAComponent() != null));
	}
	
	public static boolean isAgent(GameObject obj) {
		return (obj.getInteractionComponent() != null);
	}
	
	public static boolean isZone(GameObject obj) {
		return ((obj.getInteractionComponent() != null) &&
				(obj.getPhysicsComponent() == null));
	}
	
	public static boolean isObstacle(GameObject obj) {
		return (obj.getPhysicsComponent() != null);
	}
	
	public static boolean isProp(GameObject obj) {
		return ((obj.getPhysicsComponent() != null) &&
				(obj.getGraphicsComponent() != null) &&
				(obj.getIAComponent() == null));
	}
	
	public static boolean isDecoration(GameObject obj) {
		return ((obj.getGraphicsComponent() != null) &&
				(obj.getInteractionComponent() == null) &&
				(obj.getPhysicsComponent() == null) &&
				(obj.getIAComponent() == null));
	}
	
	
	public static boolean testBBoxOverlap(GameObject obj, GameObject other) {
		
		PhysicsComponent objPhy = obj.getPhysicsComponent();
		PhysicsComponent otherPhy = other.getPhysicsComponent();
		if ((objPhy == null) || (otherPhy == null)) {
			return false;
		}
			
		Cell cornerCell = objPhy.getBoundingBox();
		Cell otherCornerCell = otherPhy.getBoundingBox();
		if (cornerCell.equals(otherCornerCell)) {
			return !obj.equals(other);
		} else {
			return false;
		}

	}
	
}
