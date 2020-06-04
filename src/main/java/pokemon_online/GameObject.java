package pokemon_online;

import java.util.ArrayList;
import java.util.Collection;

import pokemon_online.game.Controller;
import pokemon_online.game.GameObjectListener;
import pokemon_online.game.GameObjectsContainer;
import pokemon_online.game.GraphicsComponent;
import pokemon_online.game.PhysicsComponent;
import pokemon_online.game.ia.IAComponent;

/**
 * 
 */

/**
 * @author Cecchi
 *
 */
public class GameObject {

	private final Collection<GameObjectListener> listeners;
	
	private int x;
	
	private int y;
	
	
	protected GraphicsComponent grapComp;
	
	protected PhysicsComponent physComp;
	
	protected IAComponent iaComp;
	
	protected final Controller ctrl;
	
	protected int speedX; // In pxl/tick

	protected int speedY; // In pxl/tick
	
	/**
	 * The direction the object is facing. Doesn't have to match the moving direction;
	 */
	protected double direction;
	
	public GameObject() {
		ctrl = new Controller();
		
		listeners = new ArrayList<>();
	}

	public Controller getCtrl() {
		return ctrl;
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

	public IAComponent getIAComponent() {
		return iaComp;
	}
	
	public void setIAComponent(IAComponent iaComp) {
		this.iaComp = iaComp;
	}
	
	public GraphicsComponent getGraphicsComponent() {
		return grapComp;
	}
	
	public void setGraphicsComponent(GraphicsComponent grapComp) {
		this.grapComp = grapComp;
	}
	
	public PhysicsComponent getPhysicsComponent() {
		return physComp;
	}

	public void setPhysicsComponent(PhysicsComponent physComp) {
		this.physComp = physComp;
	}

	public void setX(int x) {
		setPosition(x, this.y);
	}
	
	public void setY(int y) {
		setPosition(this.x, y);
	}
	
	public void setPosition(int x, int y) {
		int prevX = this.x;
		int prevY = this.y;
		this.x = x;
		this.y = y;
		
		if ((prevX != x) || (prevY != y)) {
			// Notify listeners the position has changed
			for (GameObjectListener listener : listeners) {
				listener.positionChanged(this, prevX, prevY, x, y);
			}
		}
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

	public void addListener(GameObjectListener listener) {
		listeners.add(listener);
	}

	public void removeListener(GameObjectListener listener) {
		listeners.remove(listener);
	}

}
