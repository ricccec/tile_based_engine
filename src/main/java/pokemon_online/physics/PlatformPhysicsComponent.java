/**
 * 
 */
package pokemon_online.physics;

import java.util.HashSet;
import java.util.Set;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;

/**
 * @author Cecchi
 *
 */
public class PlatformPhysicsComponent extends PhysicsComponent {

	private final Set<CardinalDirection> blockedDirs;
	
	public PlatformPhysicsComponent(GameObject obj) {
		super(obj);
		blockedDirs = new HashSet<>();
	}

	public void addBlockedDirection(CardinalDirection dir) {
		blockedDirs.add(dir);
	}
	
	@Override
	public void update(GameWorld world, long dtMillisec) {
	}

	@Override
	public Cell getBoundingBox() {
		int x = getGameObject().getX();
		int y = getGameObject().getY();
		int col = GameUtils.getColumn(x);
		int row = GameUtils.getRow(y);
		return new Cell(row, col);
	}

	@Override
	public boolean checkCollision(PhysicsComponent otherPhy) {
		if (!super.checkCollision(otherPhy)) {
			return false;
		}
		
		CardinalDirection movingDir = CardinalDirection.degree2direction(otherPhy.getMovingDirection());
		for (CardinalDirection blockedDir : blockedDirs) {
			if (movingDir == blockedDir) {
				return true;
			}
		}
		return false;
	}

}
