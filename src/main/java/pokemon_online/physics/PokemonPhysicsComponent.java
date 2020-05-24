/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.Configuration;
import pokemon_online.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PokemonPhysicsComponent extends PhysicsComponent {
	
	private final DirectionFilter dirFilter;
	
	public PokemonPhysicsComponent(GameObject obj) {
		super(obj);
		
		dirFilter = new DirectionFilter(obj.getController());
	}

	@Override
	public void update(GameWorld world, long dtMillisec) {
		
		dirFilter.updateControllerDirection();
		
		// FIXME dtMillisec is ignored
		
		int residueDist = getSpeed();
		if (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0)) {
			assert(getSpeed() > 0);
			// A movement from the previous tick is still ongoing
			// Complete the movement
			int prevPos = (getDirection().isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			resolveCollision(world);
			int currPos = (getDirection().isAlongX() ? obj.getX() : obj.getY());
			residueDist -= Math.abs(prevPos - currPos);
		}
		
		// The object's controller state is read only when the object reach the next cell
		if (((obj.getX() % 32) == 0) && ((obj.getY() % 32) == 0))  {
			if (dirFilter.getControllerDirection() == null) {
				obj.setSpeedX(0);
				obj.setSpeedY(0);
			} else {
				setVelocity(dirFilter.getControllerDirection(), Configuration.PLAYER_SPEED);
			}
		}
//		Controller controller = obj.getController();
//		if (controller != null) {
//			if (((obj.getX() % 32) == 0) && ((obj.getY() % 32) == 0)) {
//				// Player is right in the middle of a cell: update speed based on controllers
//				if (controller.allDeactivated(MOVE_LEFT, MOVE_DWN, MOVE_RIGHT, MOVE_UP)) {
//					obj.setSpeedX(0);
//					obj.setSpeedY(0);
//				} else if (controller.isActive(MOVE_RIGHT) && (!isMovingRight())) {
//					setVelocity(Direction.DIR_RIGHT, Configuration.PLAYER_SPEED);
//				} else if (controller.isActive(MOVE_DWN) && (!isMovingDown())) {
//					setVelocity(Direction.DIR_DOWN, Configuration.PLAYER_SPEED);
//				} else if (controller.isActive(MOVE_LEFT) && (!isMovingLeft())) {
//					setVelocity(Direction.DIR_LEFT, Configuration.PLAYER_SPEED);
//				} else if (controller.isActive(MOVE_UP) && (!isMovingUp())) {
//					setVelocity(Direction.DIR_UP, Configuration.PLAYER_SPEED);
//				}
//			}
//		}
		
		if ((getSpeed() == 0))
			return;
		
		// Start a new cell-by-cell movement
		while(residueDist > 0) {
			int prevPos = (getDirection().isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			resolveCollision(world);
			int currPos = (getDirection().isAlongX() ? obj.getX() : obj.getY());
			int dPxls = Math.abs(prevPos - currPos);
			if (dPxls == 0) // Block
				return;
			residueDist -= dPxls;
		}
		
	}
	
	

	private void resolveCollision(GameWorld world) {
		int cornerX = obj.getX();
		if ((getDirection().isAlongX() && (getDirection().sign > 0)))
			cornerX += 32;
		int cornerY = obj.getY();
		if ((getDirection().isAlongY() && (getDirection().sign > 0)))
			cornerY += 32;
		
		int cornerRow = world.getRow(cornerY);
		int cornerCol = world.getColumn(cornerX);
		
		if (world.isWalkable(cornerRow, cornerCol))
			return;
		
		// Cell is not walkable: resolve collision
		int newRow = cornerRow - (getDirection().isAlongY() ? getDirection().sign : 0);
		int newCol = cornerCol - (getDirection().isAlongX() ? getDirection().sign : 0);
		
		obj.setX(world.getX(newCol));
		obj.setY(world.getY(newRow));

	}
	
	private void moveOneCell(GameWorld world, int dPxlsMax) {
		
		// Compute distance from next cell
		int currPos = (getDirection().isAlongX() ? obj.getX() : obj.getY());
		int dPxls = 0;
		if ((currPos % 32) == 0) {
			dPxls = 32;
		} else {
			if (getDirection().sign*currPos > 0) {
				dPxls = 32 - (Math.abs(currPos) % 32);
			} else {
				dPxls = (Math.abs(currPos) % 32);
			}
		}
		// Cannot move more than dPxlsMax
		dPxls = Math.min(dPxlsMax, dPxls);
		
		if (getDirection().isAlongX()) {
			obj.setX(obj.getX() + getDirection().sign*dPxls);
		} else {
			obj.setY(obj.getY() + getDirection().sign*dPxls);
		}
		
	}

	public boolean isMovingRight() {
		return ((getSpeed() > 0) && (getDirection() == Direction.DIR_RIGHT));
	}
	
	public boolean isMovingDown() {
		return ((getSpeed() > 0) && (getDirection() == Direction.DIR_DOWN));
	}
	
	
	public boolean isMovingLeft() {
		return ((getSpeed() > 0) && (getDirection() == Direction.DIR_LEFT));
	}
	
	public boolean isMovingUp() {
		return ((getSpeed() > 0) && (getDirection() == Direction.DIR_UP));
	}
	
	private Direction getDirection() {
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
	
	private void setVelocity(Direction dir, int speed) {
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
	
	private int getSpeed() {
		return (int)Math.ceil(Math.sqrt(Math.pow(obj.getSpeedX(), 2) + Math.pow( obj.getSpeedY(), 2)));
	}
	
}
