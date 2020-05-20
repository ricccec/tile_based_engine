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
	
	protected int state;
	
	public int getState() {
		return state;
	}

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

}
