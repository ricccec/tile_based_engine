/**
 * 
 */
package pokemon_online.land.zone;

import java.awt.Color;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.interaction.InteractionComponent;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.EventHandler;
import pokemon_online.game.interaction.event.Event.Type;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
import pokemon_online.game.rendering.SpriteGraphicsComponent.GraphicsState;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.CardinalDirection;
import pokemon_online.physics.GridBoundPhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class JumpZone extends Zone {

	private final CardinalDirection dir;
	
	public JumpZone(CardinalDirection dir) {
		this.dir = dir;
	}

	@Override
	protected void onExiting(GameWorld world, GameObject zone, GameObject entity) {
		if ((dir == CardinalDirection.DIR_LEFT) || (dir == CardinalDirection.DIR_RIGHT)) {
			assert(entity.getPhysicsComponent() != null);
			assert(entity.getPhysicsComponent() instanceof GridBoundPhysicsComponent);
			assert(entity.getPhysicsComponent().isMoving());
		}
	}

	@Override
	protected void onEntering(GameWorld world, GameObject zone, GameObject entity) {
		
		assert(entity.getPhysicsComponent() != null);
		assert(entity.getPhysicsComponent().isMoving());
		
		double movingDirDegree = entity.getPhysicsComponent().getMovingDirection();
		CardinalDirection movingDir = CardinalDirection.degree2direction(movingDirDegree);
		if (movingDir != dir) {
			return;
		}
		
		GraphicsComponent grapComp = entity.getGraphicsComponent();
		if ((grapComp != null) && (grapComp instanceof SpriteGraphicsComponent)) {
			SpriteGraphicsComponent sprGrapComp = (SpriteGraphicsComponent)grapComp;
			sprGrapComp.pushState(GraphicsState.JUMPING);
		}
		
		if ((dir == CardinalDirection.DIR_LEFT) || (dir == CardinalDirection.DIR_RIGHT)) {
			GridBoundPhysicsComponent jumpPhy = new GridBoundPhysicsComponent(entity);
			jumpPhy.enableCollisionCheck(false);
			entity.pushPhysicsComponent(jumpPhy);
			jumpPhy.move(dir, 2);
		}
		
	}

	@Override
	protected Color getColor() {
		return new Color(0, 0, 1f, .5f);
	}
	
	
	
	
}
