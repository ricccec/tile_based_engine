package pokemon_online;

import org.apache.commons.io.filefilter.AndFileFilter;

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
	public double getMovingDirectionDegrees() {
		double angRad = Math.atan2(-speedY, speedX);
		double angNorm = angRad/Math.PI;
		double angDegr = ((angNorm < 0) ? 360 : 0) + 180*angNorm;
		
		return angDegr;
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

}
