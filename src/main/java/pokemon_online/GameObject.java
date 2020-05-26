package pokemon_online;

import pokemon_online.game.Controller;
import pokemon_online.game.GraphicsComponent;
import pokemon_online.game.PhysicsComponent;

/**
 * 
 */

/**
 * @author Cecchi
 *
 */
public abstract class GameObject {

	protected GraphicsComponent grapComp;
	
	protected PhysicsComponent physComp;
	
	protected Controller ctrl;
	
	protected int x;
	
	protected int y;
	
	protected int speedX; // In pxl/tick

	protected int speedY; // In pxl/tick
	
	/**
	 * The direction the object is facing. Doesn't have to match the moving direction;
	 */
	protected double direction;

	/**
	 * @return the object's X coordinate in the world's space
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the object's Y coordinate in the world's space
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return the moving direction, in degrees
	 */
	public double getMovingDirection() {
		double angRad = Math.atan2(-speedY, speedX);
		double angNorm = angRad/Math.PI;
		double angDegr = ((angNorm < 0) ? 360 : 0) + 180*angNorm;
		
		return angDegr;
	}
	
	/**
	 * @return the direction the object is facing, in degrees
	 */
	public double getFacingDirection() {
		return direction;
	}
	
	public void setFacingDirection(int dirDegree) {
		direction = dirDegree;
	}
	
	public Controller getController() {
		return ctrl;
	}

	public GraphicsComponent getGraphicsComponent() {
		return grapComp;
	}
	
	public PhysicsComponent getPhysicsComponent() {
		return physComp;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public boolean isMoving() {
		return ((getSpeedX() != 0) || (getSpeedY() != 0));
	}

}
