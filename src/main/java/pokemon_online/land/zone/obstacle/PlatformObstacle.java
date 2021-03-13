/**
 * 
 */
package pokemon_online.land.zone.obstacle;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.CardinalDirection;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PlatformObstacle extends FixedObstacle {

	private final Set<CardinalDirection> blockedDirs;
	
	public PlatformObstacle(Cell position) {
		super(position);
		
		blockedDirs = new HashSet<>();
	}
	
	public void addBlockedDirection(CardinalDirection dir) {
		blockedDirs.add(dir);
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
	
	@Override
	protected Color getColor() {
		return GameUtils.getColor(Color.ORANGE, .5f);
	}

}
