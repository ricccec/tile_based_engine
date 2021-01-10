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
		if ((bBoxCol == null) || (bBoxRow == null)) {
			bBoxCol = GameUtils.getColumn(obj.getX());
			bBoxRow = GameUtils.getRow(obj.getY());
		}
		
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
			int newRow = cornerRow - (getMovingDirection().isAlongY() ? getMovingDirection().sign : 0);
			int newCol = cornerCol - (getMovingDirection().isAlongX() ? getMovingDirection().sign : 0);
			
			obj.setPosition(GameUtils.getX(newCol), GameUtils.getY(newRow));

			// Update bounding box
			bBoxCol = GameUtils.getColumn(obj.getX());
			bBoxRow = GameUtils.getRow(obj.getY());
			notifyBoundingBoxChanged(getBoundingBox());
		}

	}
	
	public void moveOneCell(GameWorld world, int dPxlsMax) {
		
		assert(dPxlsMax > 0);
		
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
		
		assert(dPxls > 0);
		
		if (getMovingDirection().isAlongX()) {
			obj.setX(obj.getX() + getMovingDirection().sign*dPxls);
		} else {
			obj.setY(obj.getY() + getMovingDirection().sign*dPxls);
		}
		
		// Bounding box changed
		int bBoxX = obj.getX();
		if (getMovingDirection().isAlongX() &&
			(getMovingDirection().sign > 0) &&
			((obj.getX() % 32) != 0)) { // Object is moving (in the +X direction) and has not reached the next cell 
			bBoxX += 32;
		}
		bBoxCol = GameUtils.getColumn(bBoxX);
		int bBoxY = obj.getY();
		if (getMovingDirection().isAlongY()
			&& (getMovingDirection().sign > 0) &&
			((obj.getY() % 32) != 0)) { // Object is moving (in the +Y direction) and has not reached the next cell 
			bBoxY += 32;
		}
		bBoxRow = GameUtils.getRow(bBoxY);
//		System.out.println("Y: " + bBoxY + " row: " + bBoxRow);
		notifyBoundingBoxChanged(getBoundingBox());
		
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
		return Direction.degree2direction(movingDir);
	}
	
	public Direction getFacingDirection() {
		// FIXME Merge with the previous method
		double movingDir = obj.getFacingDirection();
		return Direction.degree2direction(movingDir);
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

	@Override
	public void positionChanged(GameObject obj, int prevX, int prevY, int currX, int currY) {
		bBoxRow = null;
		bBoxCol = null;
	}
	
}
