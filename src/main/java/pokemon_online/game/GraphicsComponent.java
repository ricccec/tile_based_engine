/**
 * 
 */
package pokemon_online.game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.Component;
import pokemon_online.GameObject;
import pokemon_online.ResourcesManager;
import pokemon_online.game.rendering.StateAnimation;
import pokemon_online.game.rendering.Viewport;

/**
 * @author Cecchi
 *
 */
public class GraphicsComponent extends Component { // TODO Make abstract
	
	private enum State {
		IDLE,
		WALKING;
	}
	
	private final Map<State, StateAnimation> animations;
	
	private State currState; // FIXME Use a real FSM
	
	public GraphicsComponent(GameObject obj) {
		super(obj);
		
		currState = State.IDLE;
		
		animations = new HashMap<>();
		
		animations.put(State.IDLE, new StateAnimation(State.IDLE.toString()));
		animations.get(State.IDLE).getAnimation(0).addSprite("F Allenatrice_E_Stop.gif");
		animations.get(State.IDLE).getAnimation(90).addSprite("F Allenatrice_N_Stop.gif");
		animations.get(State.IDLE).getAnimation(180).addSprite("F Allenatrice_O_Stop.gif");
		animations.get(State.IDLE).getAnimation(270).addSprite("F Allenatrice_S_Stop.gif");
		
		animations.put(State.WALKING, new StateAnimation(State.WALKING.toString()));
		animations.get(State.WALKING).getAnimation(0).addSprite("F Allenatrice_E_Walk.gif").addSprite("F Allenatrice_E_Stop.gif");
		animations.get(State.WALKING).getAnimation(90).addSprite("F Allenatrice_N_Walk.gif").addSprite("F Allenatrice_N_Stop.gif");
		animations.get(State.WALKING).getAnimation(180).addSprite("F Allenatrice_O_Walk.gif").addSprite("F Allenatrice_O_Stop.gif");
		animations.get(State.WALKING).getAnimation(270).addSprite("F Allenatrice_S_Walk.gif").addSprite("F Allenatrice_S_Stop.gif");
	}
	
	public void render(Graphics2D grap, Viewport viewport) {
		int scrX = viewport.getScreenX() + obj.getX();
		int scrY = viewport.getScreenY() + obj.getY();
		
		// Draw object sprite
		int objDir = (int)(90*Math.round((obj.getFacingDirection()/90)));
		String imgName = animations.get(currState).getCurrentSprinte(objDir);
		// TODO Sprite and bounding box might have different size: draw the image in such a way that its center is aligned with the bb's center
		Image tileImg = ResourcesManager.getMgr().getImage(imgName, 2);
		grap.drawImage(tileImg,scrX, scrY, null);
	}

	public void updateAnimation(long dt) {
		switch (currState) {
			// Change state?
			case IDLE:
				if (obj.isMoving()) {
					currState = State.WALKING;
					animations.get(currState).reset();
				}
				break;
			case WALKING:
				if (!obj.isMoving()) {
					currState = State.IDLE;
					animations.get(currState).reset();
				}
				break;
		}
		animations.get(currState).updateAnimation(dt);
	}

}
