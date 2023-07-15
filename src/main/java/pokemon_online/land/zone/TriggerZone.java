/**
 * 
 */
package pokemon_online.land.zone;

import java.awt.Color;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.land.zone.obstacle.FixedObstacle;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class TriggerZone extends Zone {

	private boolean isTriggered;
	
	public GameObject buildTarget(Cell cell) {
		GameObject result = new FixedObstacle(cell) {
			
			@Override
			protected Color getColor() {
				if (isTriggered) {
					return new Color(1f, 0, 0, .25f);
				} else {
					return new Color(0, 0, 0, 0);
				}
			}
			
			@Override
			public boolean checkCollision(PhysicsComponent otherPhy) {
				if (!super.checkCollision(otherPhy)) {
					return false;
				}
				return isTriggered;
			}
		};
		
		return result;
	}

	@Override
	protected void onEntering(GameWorld world, GameObject zone, GameObject entity) {
//		System.out.println("Entering");
		isTriggered = true;
	}

	@Override
	protected void onExiting(GameWorld world, GameObject receiver, GameObject sender) {
		isTriggered = false;
//		System.out.println("Exiting");
	}

	@Override
	protected Color getDebugColor() {
		return new Color(1f, 1f, 0f, .25f);
	}
}
