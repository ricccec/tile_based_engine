/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.Event.Type;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;

/**
 * Represents the physics of an Entity that can only move in the UP, DOWN, RIGHT
 * and LEFT {@link CardinalDirection}, each time walking an integer number of cells of a grid
 * (i.e. the Entity cannot stop nor can change direction while crossing between
 * two cells)
 * 
 * @author Cecchi
 *
 */
public class GridBoundPhysicsComponent extends PhysicsComponent {
	
	/**
	 * The Entity moving direction.
	 */
	private CardinalDirection movingDirection;
	
	private int cellsCount;
	
	private boolean checkCollision;
	
	public GridBoundPhysicsComponent(GameObject obj) {
		super(obj);
		checkCollision = true;
	}
	
	public void enableCollisionCheck(boolean b) {
		checkCollision = b;
	}
	
	public void move(CardinalDirection dir) {
		move(dir, 1);
	}
	
	public void move(CardinalDirection dir, int cells2Move) {
		if (movingDirection == null) {
			// TODO Queue commands?
			movingDirection = dir;
			this.cellsCount = cells2Move;
			setObjectVelocity(movingDirection, Configuration.PLAYER_SPEED); // FIXME custom speed
			obj.setState(State.FROZEN);
		}
	}
	
	public void moveUp() {
		move(CardinalDirection.DIR_UP);
	}
	
	public void moveRight() {
		move(CardinalDirection.DIR_RIGHT);
	}
	
	public void moveDown() {
		move(CardinalDirection.DIR_DOWN);
	}
	
	public void moveLeft() {
		move(CardinalDirection.DIR_LEFT);
	}
	
	public boolean isMoving() {
		return (movingDirection != null);
	}
	

	@Override
	public void update(GameWorld world, long dtMillisec) {
		// FIXME dtMillisec is ignored
		
		// Complete any previous movement
		int residueDist = Configuration.PLAYER_SPEED;//phyComp.getSpeed();
		if (isCrossingCells()) {
			// A movement from the previous tick is still ongoing
			assert(movingDirection != null);
			 assert((getSpeedX() != 0) || (getSpeedY() != 0));
			assert(obj.getState() != State.ACTIVE); // This object can't interact while moving
			
			// Complete the movement
			int prevPos = (getObjectMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			if (checkCollision) resolveCollision(world);
			int currPos = (getObjectMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			residueDist -= Math.abs(prevPos - currPos);
		}
		
		if (movingDirection == null) {
			return;
		}
		
		if (!isCrossingCells()) {
			// Object is centered on a cell
			if (cellsCount == 0) {
				// Movement completed, stop object
				movingDirection = null;
				obj.setState(State.ACTIVE);
				obj.popPhysicsComponent();
				obj.notifyEvent(world, new Event(Type.PUSH_COMPLETED));
				return;
			} else {
				cellsCount--;
			}
		}
		
		// Start a new cell-by-cell movement
		while(residueDist > 0) {  // Each iteration move the Entity one cell and resolve the collisions
			int prevPos = (getObjectMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			moveOneCell(world, residueDist);
			if (checkCollision) resolveCollision(world);
			int currPos = (getObjectMovingDirection().isAlongX() ? obj.getX() : obj.getY());
			int dPxls = Math.abs(prevPos - currPos);
			if (dPxls == 0) // Block
				break;
			residueDist -= dPxls;
		}
		
	}
	
	/**
	 * Return the velocity (modulus of the speed) of the associated Entity
	 * @return
	 */
	public final int getObjectSpeed() {
		return (int)Math.ceil(Math.sqrt(Math.pow(getSpeedX(), 2) + Math.pow(getSpeedY(), 2)));
	}
	
	public final void setObjectVelocity(CardinalDirection dir, int speed) {
		switch(dir) {
			case DIR_DOWN:
				setSpeedX(0);
				setSpeedY(speed);
				break;
			case DIR_LEFT:
				setSpeedX(-speed);
				setSpeedY(0);
				break;
			case DIR_RIGHT:
				setSpeedX(speed);
				setSpeedY(0);
				break;
			case DIR_UP:
				setSpeedX(0);
				setSpeedY(-speed);
				break;
		}
	}
	
	public final CardinalDirection getObjectMovingDirection() {
		double movingDir = getMovingDirection();
		return CardinalDirection.degree2direction(movingDir);
	}
	
	public final boolean isCrossingCells() {
		return (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0));
	}
	
	@Override
	public final Cell getBoundingBox() { // FIXME Move to superclass?

			int bBoxX = obj.getX();
			if (getObjectMovingDirection().isAlongX() &&
				(getObjectMovingDirection().sign > 0) &&
				((obj.getX() % 32) != 0)) { // Object is moving (in the +X direction) and has not reached the next cell 
				bBoxX += 32;
			}
			
			int bBoxY = obj.getY();
			if (getObjectMovingDirection().isAlongY()
				&& (getObjectMovingDirection().sign > 0) &&
				((obj.getY() % 32) != 0)) { // Object is moving (in the +Y direction) and has not reached the next cell 
				bBoxY += 32;
			}
			
			return new Cell(GameUtils.getRow(bBoxY), GameUtils.getColumn(bBoxX));

	}

	/**
	 * Move the associated Entity by one cell if the movement requires at most a
	 * displacement of dPxlsMax. Otherwise moves the Entity by dPxlsMax pixels.
	 * 
	 * @param world
	 * @param dPxlsMax
	 */
	public final void moveOneCell(GameWorld world, int dPxlsMax) {
		
		assert(dPxlsMax > 0);
		
		// Compute distance from next cell
		int currPos = (getObjectMovingDirection().isAlongX() ? obj.getX() : obj.getY());
		int dPxls = 0;
		if ((currPos % 32) == 0) {
			dPxls = 32;
		} else {
			if (getObjectMovingDirection().sign*currPos > 0) {
				dPxls = 32 - (Math.abs(currPos) % 32);
			} else {
				dPxls = (Math.abs(currPos) % 32);
			}
		}
		// Cannot move more than dPxlsMax
		dPxls = Math.min(dPxlsMax, dPxls);
		
		assert(dPxls > 0);
		
		if (getObjectMovingDirection().isAlongX()) {
			obj.setX(obj.getX() + getObjectMovingDirection().sign*dPxls);
		} else {
			obj.setY(obj.getY() + getObjectMovingDirection().sign*dPxls);
		}
		
		notifyBoundingBoxChanged(getBoundingBox());
		
	}
	
	public final void resolveCollision(GameWorld world) {
		
		Cell cornerCell = getBoundingBox();
		int cornerRow = cornerCell.getRow();
		int cornerCol = cornerCell.getColumn();
		
		// Check collision
		boolean collides = !world.isWalkable(cornerRow, cornerCol);
		if (!collides) {
			// Check object-with-object collision
			for (GameObject otherObj : world.getObstacles(cornerRow, cornerCol)) {
				assert(otherObj.getPhysicsComponent() != null);
				if (otherObj.getPhysicsComponent().checkCollision(this)) {//if (!otherObj.equals(obj)) {
					
					collides = true;
					break;
				}
			}
		}
		
		if (collides) {
			// Cell is not walkable: resolve collision
			int newRow = cornerRow - (getObjectMovingDirection().isAlongY() ? getObjectMovingDirection().sign : 0);
			int newCol = cornerCol - (getObjectMovingDirection().isAlongX() ? getObjectMovingDirection().sign : 0);
			
			obj.setPosition(GameUtils.getX(newCol), GameUtils.getY(newRow));

			// Bounding box changed
			notifyBoundingBoxChanged(getBoundingBox());
		}

	}
	

}
