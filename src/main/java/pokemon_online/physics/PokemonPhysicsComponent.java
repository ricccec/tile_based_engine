/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PokemonPhysicsComponent extends PhysicsComponent {
	
	private final DirectionFilter dirFilter;
	
	private PkmnPhyState state;
	
	public PokemonPhysicsComponent(GameObject obj) {
		super(obj);
		
		dirFilter = new DirectionFilter(obj.getController());
		state = new PkmnPhyStateIdle(this); // Use a Factory for stateless states
	}

	@Override
	public void update(GameWorld world, long dtMillisec) {
		
		dirFilter.updateControllerDirection();
		
		PkmnPhyState newState = state.updateState(obj, world, dtMillisec, dirFilter.getControllerDirection());
		if (newState != null) {
			// State has changed
			state = newState;
			state.enterState();
		}
		
	}
	
	

	public void resolveCollision(GameWorld world) {
		int cornerX = obj.getX();
		if ((getMovingDirection().isAlongX() && (getMovingDirection().sign > 0)))
			cornerX += 32;
		int cornerY = obj.getY();
		if ((getMovingDirection().isAlongY() && (getMovingDirection().sign > 0)))
			cornerY += 32;
		
		int cornerRow = world.getRow(cornerY);
		int cornerCol = world.getColumn(cornerX);
		
		if (world.isWalkable(cornerRow, cornerCol))
			return;
		
		// Cell is not walkable: resolve collision
		int newRow = cornerRow - (getMovingDirection().isAlongY() ? getMovingDirection().sign : 0);
		int newCol = cornerCol - (getMovingDirection().isAlongX() ? getMovingDirection().sign : 0);
		
		obj.setX(world.getX(newCol));
		obj.setY(world.getY(newRow));

	}
	
	public void moveOneCell(GameWorld world, int dPxlsMax) {
		
		// Compute distance from next cell
		int currPos = (getMovingDirection().isAlongX() ? obj.getX() : obj.getY());
		int dPxls = 0;
		if ((currPos % 32) == 0) {
			dPxls = 32;
		} else {
			if (getMovingDirection().sign*currPos > 0) {
				dPxls = 32 - (Math.abs(currPos) % 32);
			} else {
				dPxls = (Math.abs(currPos) % 32);
			}
		}
		// Cannot move more than dPxlsMax
		dPxls = Math.min(dPxlsMax, dPxls);
		
		if (getMovingDirection().isAlongX()) {
			obj.setX(obj.getX() + getMovingDirection().sign*dPxls);
		} else {
			obj.setY(obj.getY() + getMovingDirection().sign*dPxls);
		}
		
	}

	public boolean isMovingRight() {
		return ((getSpeed() > 0) && (getMovingDirection() == Direction.DIR_RIGHT));
	}
	
	public boolean isMovingDown() {
		return ((getSpeed() > 0) && (getMovingDirection() == Direction.DIR_DOWN));
	}
	
	
	public boolean isMovingLeft() {
		return ((getSpeed() > 0) && (getMovingDirection() == Direction.DIR_LEFT));
	}
	
	public boolean isMovingUp() {
		return ((getSpeed() > 0) && (getMovingDirection() == Direction.DIR_UP));
	}
	
	public Direction getMovingDirection() {
		double movingDir = obj.getMovingDirection();
		if (movingDir <= 45) {
			return Direction.DIR_RIGHT;
		}
		if ((movingDir > 45) && (movingDir <= 135)) {
			return Direction.DIR_UP;
		}
		if ((movingDir > 135) && (movingDir <= 225)) {
			return Direction.DIR_LEFT;
		}
		if ((movingDir > 225) && (movingDir <= 315)) {
			return Direction.DIR_DOWN;
		}
		// direction is > 315
		return Direction.DIR_RIGHT;
	}
	
	public Direction getFacingDirection() {
		// FIXME Merge with the previous method
		double movingDir = obj.getFacingDirection();
		if (movingDir <= 45) {
			return Direction.DIR_RIGHT;
		}
		if ((movingDir > 45) && (movingDir <= 135)) {
			return Direction.DIR_UP;
		}
		if ((movingDir > 135) && (movingDir <= 225)) {
			return Direction.DIR_LEFT;
		}
		if ((movingDir > 225) && (movingDir <= 315)) {
			return Direction.DIR_DOWN;
		}
		// direction is > 315
		return Direction.DIR_RIGHT;
	}
	
	public void setVelocity(Direction dir, int speed) {
		switch(dir) {
			case DIR_DOWN:
				obj.setSpeedX(0);
				obj.setSpeedY(speed);
				obj.setFacingDirection(270);
				break;
			case DIR_LEFT:
				obj.setSpeedX(-speed);
				obj.setSpeedY(0);
				obj.setFacingDirection(180);
				break;
			case DIR_RIGHT:
				obj.setSpeedX(speed);
				obj.setSpeedY(0);
				obj.setFacingDirection(0);
				break;
			case DIR_UP:
				obj.setSpeedX(0);
				obj.setSpeedY(-speed);
				obj.setFacingDirection(90);
				break;
		}
	}
	
	public int getSpeed() {
		return (int)Math.ceil(Math.sqrt(Math.pow(obj.getSpeedX(), 2) + Math.pow( obj.getSpeedY(), 2)));
	}
	
}
