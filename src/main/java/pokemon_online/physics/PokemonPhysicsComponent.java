/**
 * 
 */
package pokemon_online.physics;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectListener;
import pokemon_online.game.GameWorld;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameUtils;

/**
 * Moves an Entity through a cell grids, one cell at a time. The Entity always
 * crosses an integer number of cells, and diagonal movements are not allowed.
 * 
 * @author Cecchi
 *
 */
public class PokemonPhysicsComponent extends PhysicsComponent implements GameObjectListener {
	
	private final DirectionFilter dirFilter;
	
	private PkmnPhyState state;
	
	private Integer bBoxCol;
	
	private Integer bBoxRow;
	
	public PokemonPhysicsComponent(GameObject obj) {
		super(obj);
		
		obj.addListener(this);
		bBoxCol = null;
		bBoxRow = null;
		
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
			state.enterState(world);
		}
		
	}
	
	public Cell getBoundingBox() {
//		if ((bBoxCol == null) || (bBoxRow == null)) {
//			bBoxCol = GameUtils.getColumn(obj.getX());
//			bBoxRow = GameUtils.getRow(obj.getY());
//		}
//		
		// Bounding box changed
		int bBoxX = obj.getX();
		if (getCardinalMovingDir().isAlongX() &&
			(getCardinalMovingDir().sign > 0) &&
			((obj.getX() % 32) != 0)) { // Object is moving (in the +X direction) and has not reached the next cell 
			bBoxX += 32;
		}
		bBoxCol = GameUtils.getColumn(bBoxX);
		int bBoxY = obj.getY();
		if (getCardinalMovingDir().isAlongY()
			&& (getCardinalMovingDir().sign > 0) &&
			((obj.getY() % 32) != 0)) { // Object is moving (in the +Y direction) and has not reached the next cell 
			bBoxY += 32;
		}
		bBoxRow = GameUtils.getRow(bBoxY);
		
		return new Cell(bBoxRow, bBoxCol);
		
	}

	public void resolveCollision(GameWorld world) {
		
		Cell cornerCell = getBoundingBox();
		int cornerRow = cornerCell.getRow();
		int cornerCol = cornerCell.getColumn();
		
		// Check collision
		boolean collides = !world.isWalkable(cornerRow, cornerCol);
		if (!collides) {
			// Check object-with-object collision
			for (GameObject otherObj : world.getProps(cornerRow, cornerCol)) {
				if (!otherObj.equals(obj)) {
					collides = true;
					break;
				}
			}
		}
		
		if (collides) {
			// Cell is not walkable: resolve collision
			int newRow = cornerRow - (getCardinalMovingDir().isAlongY() ? getCardinalMovingDir().sign : 0);
			int newCol = cornerCol - (getCardinalMovingDir().isAlongX() ? getCardinalMovingDir().sign : 0);
			
			obj.setPosition(GameUtils.getX(newCol), GameUtils.getY(newRow));

			// Update bounding box
//			bBoxCol = GameUtils.getColumn(obj.getX());
//			bBoxRow = GameUtils.getRow(obj.getY());
			notifyBoundingBoxChanged(getBoundingBox());
		}

	}
	
	public boolean isCrossingCells() {
		return (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0));
	}
	
	public void moveOneCell(GameWorld world, int dPxlsMax) {
		
		assert(dPxlsMax > 0);
		
		// Compute distance from next cell
		int currPos = (getCardinalMovingDir().isAlongX() ? obj.getX() : obj.getY());
		int dPxls = 0;
		if ((currPos % 32) == 0) {
			dPxls = 32;
		} else {
			if (getCardinalMovingDir().sign*currPos > 0) {
				dPxls = 32 - (Math.abs(currPos) % 32);
			} else {
				dPxls = (Math.abs(currPos) % 32);
			}
		}
		// Cannot move more than dPxlsMax
		dPxls = Math.min(dPxlsMax, dPxls);
		
		assert(dPxls > 0);
		
		if (getCardinalMovingDir().isAlongX()) {
			obj.setX(obj.getX() + getCardinalMovingDir().sign*dPxls);
		} else {
			obj.setY(obj.getY() + getCardinalMovingDir().sign*dPxls);
		}
		
		// Bounding box changed
//		int bBoxX = obj.getX();
//		if (getMovingDirection().isAlongX() &&
//			(getMovingDirection().sign > 0) &&
//			((obj.getX() % 32) != 0)) { // Object is moving (in the +X direction) and has not reached the next cell 
//			bBoxX += 32;
//		}
//		bBoxCol = GameUtils.getColumn(bBoxX);
//		int bBoxY = obj.getY();
//		if (getMovingDirection().isAlongY()
//			&& (getMovingDirection().sign > 0) &&
//			((obj.getY() % 32) != 0)) { // Object is moving (in the +Y direction) and has not reached the next cell 
//			bBoxY += 32;
//		}
//		bBoxRow = GameUtils.getRow(bBoxY);
//		System.out.println("Y: " + bBoxY + " row: " + bBoxRow);
		notifyBoundingBoxChanged(getBoundingBox());
		
	}

	public boolean isMovingRight() {
		return ((getSpeed() > 0) && (getCardinalMovingDir() == Direction.DIR_RIGHT));
	}
	
	public boolean isMovingDown() {
		return ((getSpeed() > 0) && (getCardinalMovingDir() == Direction.DIR_DOWN));
	}
	
	
	public boolean isMovingLeft() {
		return ((getSpeed() > 0) && (getCardinalMovingDir() == Direction.DIR_LEFT));
	}
	
	public boolean isMovingUp() {
		return ((getSpeed() > 0) && (getCardinalMovingDir() == Direction.DIR_UP));
	}
	
	public Direction getCardinalMovingDir() {
		double movingDir = getMovingDirection();
		return Direction.degree2direction(movingDir);
	}
	
	public void setVelocity(Direction dir, int speed) {
		switch(dir) {
			case DIR_DOWN:
				setSpeedX(0);
				setSpeedY(speed);
				obj.setFacingDirection(270);
				break;
			case DIR_LEFT:
				setSpeedX(-speed);
				setSpeedY(0);
				obj.setFacingDirection(180);
				break;
			case DIR_RIGHT:
				setSpeedX(speed);
				setSpeedY(0);
				obj.setFacingDirection(0);
				break;
			case DIR_UP:
				setSpeedX(0);
				setSpeedY(-speed);
				obj.setFacingDirection(90);
				break;
		}
	}
	
	/**
	 * Return the velocity (modulus of the speed) of the associated Entity
	 * @return
	 */
	public int getSpeed() {
		return (int)Math.ceil(Math.sqrt(Math.pow(getSpeedX(), 2) + Math.pow(getSpeedY(), 2)));
	}

	@Override
	public void positionChanged(GameObject obj, int prevX, int prevY, int currX, int currY) {
		bBoxRow = null;
		bBoxCol = null;
	}
	
}
