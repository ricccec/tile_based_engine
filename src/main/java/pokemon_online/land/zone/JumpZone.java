/**
 * 
 */
package pokemon_online.land.zone;

import java.awt.Color;
import java.awt.Graphics2D;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
import pokemon_online.game.rendering.SpriteGraphicsComponent.GraphicsState;
import pokemon_online.game.rendering.Viewport;
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
	public void renderDebugInfo(Graphics2D grap, Viewport viewport) {
		super.renderDebugInfo(grap, viewport);
		
		// Draw jump xone direction
		// Draw object ID
		grap.setColor(Color.BLUE);
		int scrX = viewport.getScreenX() + getX() + 12;
		int scrY = viewport.getScreenY() + getY() + 20; // FIXME Remove hardcoded constant
		switch(dir) {
			case DIR_DOWN:
				grap.drawString("D", scrX, scrY);
				break;
			case DIR_LEFT:
				grap.drawString("L", scrX, scrY);
				break;
			case DIR_RIGHT:
				grap.drawString("R", scrX, scrY);
				break;
			case DIR_UP:
				grap.drawString("U", scrX, scrY);
				break;
			default:
				break;
		}
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
		
		// Commented because this method might ended up being called by GameWorld#spanObject
		//assert(entity.getPhysicsComponent() != null);
		//assert(entity.getPhysicsComponent().isMoving());
		
		double movingDirDegree = entity.getPhysicsComponent().getMovingDirection();
		CardinalDirection movingDir = CardinalDirection.degree2direction(movingDirDegree);
		if (movingDir != dir) {
			return;
		}
		
		// play jumping animation
		// FIXME make this an event handler into the entity
		GraphicsComponent grapComp = entity.getGraphicsComponent();
		if ((grapComp != null) && (grapComp instanceof SpriteGraphicsComponent)) {
			SpriteGraphicsComponent sprGrapComp = (SpriteGraphicsComponent)grapComp;
			sprGrapComp.pushState(GraphicsState.JUMPING);
		}
		
		// Push a grid bound phy. comp with no collision detection and move by two cells
		if ((dir == CardinalDirection.DIR_LEFT) || (dir == CardinalDirection.DIR_RIGHT)) {
			GridBoundPhysicsComponent jumpPhy = new GridBoundPhysicsComponent(entity);
			jumpPhy.enableCollisionCheck(false);
			entity.pushPhysicsComponent(jumpPhy);
			jumpPhy.move(dir, 2);
		}
		
	}

	@Override
	protected Color getDebugColor() {
		return new Color(0, 1f, 0, .5f);
	}
	
	
	
	
}
