/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public class PlatformPhysicsComponent extends PhysicsComponent {

	public PlatformPhysicsComponent(GameObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	public void addBlockedDirection(CardinalDirection dir) {
		
	}
	
	@Override
	public void update(GameWorld world, long dtMillisec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cell getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
