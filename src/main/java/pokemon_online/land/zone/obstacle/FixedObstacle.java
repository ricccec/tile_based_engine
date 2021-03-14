/**
 * 
 */
package pokemon_online.land.zone.obstacle;

import java.awt.Color;
import java.awt.Graphics2D;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public abstract class FixedObstacle extends GameObject {

	public FixedObstacle(Cell position) {
		setX(GameUtils.getX(position.getColumn()));
		setY(GameUtils.getY(position.getRow()));
		
		setPhysicsComponent(new PhysicsComponent(this) {
			
			@Override
			public void update(GameWorld world, long dtMillisec) {}
			
			@Override
			public Cell getBoundingBox() {
				return new Cell(GameUtils.getRow(getY()), GameUtils.getColumn(getX()));
			}
			
			@Override
			public boolean checkCollision(PhysicsComponent otherPhy) {
				return FixedObstacle.this.checkCollision(otherPhy);
			}
		});
		
		setGraphicsComponent(new GraphicsComponent(this) {
			
			@Override
			public void updateAnimation(long dt) {}
			
			@Override
			public void render(Graphics2D grap, Viewport viewport) {// FIXME Move to an utility class
				if (Configuration.DEBUG) {
					int zoneScrX = viewport.getScreenX() + obj.getX();
					int zoneScrY = viewport.getScreenY() + obj.getY();
					
					
					grap.setColor(getColor());
					grap.fillRect(zoneScrX, zoneScrY, Configuration.CELL_SIZE_PXLS, Configuration.CELL_SIZE_PXLS);
				}
			}
		});
	}
	
	public boolean checkCollision(PhysicsComponent otherPhy) {
		return GameObjectUtils.testBBoxOverlap(this, otherPhy.getGameObject());
	}
	
	protected abstract Color getColor();
}
