/**
 * 
 */
package pokemon_online.physics;

import static pokemon_online.game.Control.MOVE_DWN;
import static pokemon_online.game.Control.MOVE_LEFT;
import static pokemon_online.game.Control.MOVE_RIGHT;
import static pokemon_online.game.Control.MOVE_UP;

import pokemon_online.Configuration;
import pokemon_online.GameObject;
import pokemon_online.game.Controller;
import pokemon_online.game.GameWorld;
import pokemon_online.game.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PokemonPhysicsComponent extends PhysicsComponent {

	private enum Axis {
		AXIS_X,
		AXIS_Y;
	}
	
	private enum Direction {
		// FIXME Move somewhere else
		DIR_UP(-1, Axis.AXIS_Y),
		DIR_DOWN(1, Axis.AXIS_Y),
		DIR_LEFT(-1, Axis.AXIS_X),
		DIR_RIGHT(1, Axis.AXIS_X);
		
		final int sign;
		final Axis axis;
		
		Direction(int sign, Axis axis) {
			this.sign = sign;
			this.axis = axis;
		}
		
		public boolean isAlongX() {
			return (axis == Axis.AXIS_X);
		}
		
		public boolean isAlongY() {
			return (axis == Axis.AXIS_Y);
		}
	}
	
	public PokemonPhysicsComponent(GameObject obj) {
		super(obj);
		dir = Direction.DIR_DOWN;
	}
	
	// FIXME Make these the return tipe of a method that computes tham based on speedX and speedY
	private int speed; // In px/tick
	
	private Direction dir;
	

	@Override
	public void update(GameWorld world, long dtMillisec) {
		
		// FIXME dtMillisec is ignored
		
		int residueDist = speed;
		if (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0)) {
			assert(speed > 0);
			// A movement from the previous tick is still ongoing
			// Complete the movement
			int prevPos = (dir.isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			resolveCollision(world);
			int currPos = (dir.isAlongX() ? obj.getX() : obj.getY());
			residueDist -= Math.abs(prevPos - currPos);
		}
		
		// The object's controller state is read only when the object reach the next cell
		Controller controller = obj.getController();
		if (controller != null) {
			if (((obj.getX() % 32) == 0) && ((obj.getY() % 32) == 0)) {
				// Player is right in the middle of a cell: update speed based on controllers
				if (controller.allDeactivated(MOVE_LEFT, MOVE_DWN, MOVE_RIGHT, MOVE_UP)) {
					speed = 0;
				} else if (controller.isActive(MOVE_RIGHT) && (!isMovingRight())) {
					speed = Configuration.PLAYER_SPEED;
					dir = Direction.DIR_RIGHT;
				} else if (controller.isActive(MOVE_DWN) && (!isMovingDown())) {
					speed = Configuration.PLAYER_SPEED;
					dir = Direction.DIR_DOWN;
				} else if (controller.isActive(MOVE_LEFT) && (!isMovingLeft())) {
					speed = Configuration.PLAYER_SPEED;
					dir = Direction.DIR_LEFT;
				} else if (controller.isActive(MOVE_UP) && (!isMovingUp())) {
					speed = Configuration.PLAYER_SPEED;
					dir = Direction.DIR_UP;
				}
			}
		}
		
		if ((speed == 0))
			return;
		
		// Start a new cell-by-cell movement
		while(residueDist > 0) {
			int prevPos = (dir.isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			resolveCollision(world);
			int currPos = (dir.isAlongX() ? obj.getX() : obj.getY());
			int dPxls = Math.abs(prevPos - currPos);
			if (dPxls == 0) // Block
				return;
			residueDist -= dPxls;
		}
		
	}
	
	private void resolveCollision(GameWorld world) {
		int cornerX = obj.getX();
		if ((dir.isAlongX() && (dir.sign > 0)))
			cornerX += 32;
		int cornerY = obj.getY();
		if ((dir.isAlongY() && (dir.sign > 0)))
			cornerY += 32;
		
		int cornerRow = world.getRow(cornerY);
		int cornerCol = world.getColumn(cornerX);
		
		if (world.isWalkable(cornerRow, cornerCol))
			return;
		
		// Cell is not walkable: resolve collision
		int newRow = cornerRow - (dir.isAlongY() ? dir.sign : 0);
		int newCol = cornerCol - (dir.isAlongX() ? dir.sign : 0);
		
		obj.setX(world.getX(newCol));
		obj.setY(world.getY(newRow));

	}
	
	private void moveOneCell(GameWorld world, int dPxlsMax) {
		
		// Compute distance from next cell
		int currPos = (dir.isAlongX() ? obj.getX() : obj.getY());
		int dPxls = 0;
		if ((currPos % 32) == 0) {
			dPxls = 32;
		} else {
			if (dir.sign*currPos > 0) {
				dPxls = 32 - (Math.abs(currPos) % 32);
			} else {
				dPxls = (Math.abs(currPos) % 32);
			}
		}
		// Cannot move more than dPxlsMax
		dPxls = Math.min(dPxlsMax, dPxls);
		
		if (dir.isAlongX()) {
			obj.setX(obj.getX() + dir.sign*dPxls);
		} else {
			obj.setY(obj.getY() + dir.sign*dPxls);
		}
		
	}

	public boolean isMovingRight() {
		return ((speed > 0) && (dir == Direction.DIR_RIGHT));
	}
	
	public boolean isMovingDown() {
		return ((speed > 0) && (dir == Direction.DIR_DOWN));
	}
	
	
	public boolean isMovingLeft() {
		return ((speed > 0) && (dir == Direction.DIR_LEFT));
	}
	
	public boolean isMovingUp() {
		return ((speed > 0) && (dir == Direction.DIR_UP));
	}
	
}
