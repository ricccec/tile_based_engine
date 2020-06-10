package pokemon_online.game.rendering;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.GameObject;
import pokemon_online.ResourcesManager;

public class SpriteGraphicsComponent extends GraphicsComponent {

	public enum GraphicsState { // FIXME Move somewhere else
		IDLE,
		WALKING;
		
		private static final Map<String, GraphicsState> STR_2_STATE;
		static {
			STR_2_STATE = new HashMap<>();
			STR_2_STATE.put("idle", IDLE);
			STR_2_STATE.put("walking", WALKING);
		}
		
		public static GraphicsState getStateByName(String str) {
			return STR_2_STATE.get(str);
		}
	}
	
	private final Map<GraphicsState, StateAnimation> animations;
	
	private GraphicsState currState; // FIXME Use a real FSM
	
	public SpriteGraphicsComponent(GameObject obj) {
		super(obj);
		
		currState = GraphicsState.IDLE;
		
		animations = new HashMap<>();
	}
	
	public void addGraphicsState(StateAnimation state) {
		GraphicsState stateName = state.getState();
		animations.put(stateName, state);
	}
	
	public void render(Graphics2D grap, Viewport viewport) {
		
		StateAnimation graphState = animations.get(currState);
		if (graphState == null) {
			// TODO warn
			return;
		}
		
		int scrX = viewport.getScreenX() + obj.getX();
		int scrY = viewport.getScreenY() + obj.getY();
		
		// Draw object sprite
		int objDir = (int)(90*Math.round((obj.getFacingDirection()/90)));
		String imgName = graphState.getCurrentSprinte(objDir);
		// TODO Sprite and bounding box might have different size: draw the image in such a way that its center is aligned with the bb's center
		Image tileImg = ResourcesManager.getMgr().getImage(imgName, 2);
		grap.drawImage(tileImg,scrX, scrY, null);
	}

	public void updateAnimation(long dt) {
		switch (currState) {
			// Change state?
			case IDLE:
				if (obj.isMoving()) {
					currState = GraphicsState.WALKING;
					resetStateAnimation();
				}
				break;
			case WALKING:
				if (!obj.isMoving()) {
					currState = GraphicsState.IDLE;
					resetStateAnimation();
				}
				break;
		}
		if (animations.containsKey(currState)) {
			// TODO Use a default animation in else?
			animations.get(currState).updateAnimation(dt);
		}
	}
	
	private void resetStateAnimation() {
		if (animations.containsKey(currState)) {
			animations.get(currState).reset();
		}
	}

}
