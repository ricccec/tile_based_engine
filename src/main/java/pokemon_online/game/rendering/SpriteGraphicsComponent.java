package pokemon_online.game.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import pokemon_online.Configuration;
import pokemon_online.ResourcesManager;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.land.CroppedImage;
import pokemon_online.physics.PhysicsComponent;

public class SpriteGraphicsComponent extends GraphicsComponent {

	public enum GraphicsState { // FIXME Move somewhere else
		IDLE,
		WALKING,
		JUMPING;
		
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
	
	private Stack<GraphicsState> currState; // FIXME Use a real FSM
	
	public SpriteGraphicsComponent(GameObject obj) {
		super(obj);
		
		currState = new Stack<>();
		currState.push(GraphicsState.IDLE);
		
		animations = new HashMap<>();
	}
	
	public void addGraphicsState(StateAnimation state) {
		GraphicsState stateName = state.getState();
		animations.put(stateName, state);
	}
	
	public void setState(GraphicsState state) {
		currState.clear();
		currState.push(state);
	}
	
	public GraphicsState getState() {
		return currState.peek();
	}
	
	public void pushState(GraphicsState state) {
		currState.push(state);
	}
	
	public void popState() {
		currState.pop();
	}
	
	public void render(Graphics2D grap, Viewport viewport) {
		
		StateAnimation graphState = animations.get(getState());
		if (graphState == null) {
			// TODO warn
			return;
		}
		
		if (Configuration.DEBUG) {
			// Draw bounding box (FIXME this code couples the grapic and phys. components, remove it or use a cleaner solution)
			PhysicsComponent phyComp = obj.getPhysicsComponent();
			if ((phyComp != null) ) {
				Cell bBox = phyComp.getBoundingBox();
				
				int bBoxScrX = viewport.getScreenX() + GameUtils.getX(bBox.getColumn());
				int bBoxScrY = viewport.getScreenY() + GameUtils.getY(bBox.getRow());
				
				grap.setColor(Color.RED);
				grap.fillRect(bBoxScrX, bBoxScrY, Configuration.CELL_SIZE_PXLS, Configuration.CELL_SIZE_PXLS);
			}
		}
		
		// Draw object sprite
		int scrX = viewport.getScreenX() + obj.getX();
		int scrY = viewport.getScreenY() + obj.getY();
		
		int objDir = (int)(90*Math.round((obj.getFacingDirection()/90)));
		CroppedImage objSprite = graphState.getCurrentSprinte(objDir);
		// TODO Sprite and bounding box might have different size: draw the image in such a way that its center is aligned with the bb's center
		Image tileImg = ResourcesManager.getMgr().getCroppedImage(objSprite);
		grap.drawImage(tileImg,scrX, scrY, null);
		
//		if ((phyComp != null) && (phyComp.isFrozen())) {
//			System.out.println(obj + " " + obj.getFacingDirection());
//		}
		
	}

	public void updateAnimation(long dt) {
		
		// Check animation completed
		int objDir = (int)(90*Math.round((obj.getFacingDirection()/90)));
		while ((!animations.containsKey(getState())) || (animations.get(getState()).getCurrentSprinte(objDir) == null))  {
			popState();
			resetStateAnimation();
		}
				
		// Update animation
		animations.get(getState()).updateAnimation(dt);
		
		// Change state?
		switch (getState()) {
			case IDLE:
				if ((obj.getPhysicsComponent() != null) && (obj.getPhysicsComponent().isMoving())) {
					setState(GraphicsState.WALKING);
					resetStateAnimation();
				}
				break;
			case WALKING:
				if ((obj.getPhysicsComponent() == null) || (!obj.getPhysicsComponent().isMoving())) {
					setState(GraphicsState.IDLE);
					resetStateAnimation();
				}
				break;
			default:
				break;
		}
		

	}
	
	private void resetStateAnimation() {
		if (animations.containsKey(getState())) {
			animations.get(getState()).reset();
		}
	}

}
