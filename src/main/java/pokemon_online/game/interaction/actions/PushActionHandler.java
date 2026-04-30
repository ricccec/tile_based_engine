/**
 * 
 */
package pokemon_online.game.interaction.actions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.GameWorld;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.physics.CardinalDirection;
import pokemon_online.physics.GridBoundPhysicsComponent;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PushActionHandler extends ActionHandler {

	@Override
	public boolean handleAction(GameWorld world, GameObject receiver, Action evt) {
		
		if (receiver.getState() != State.ACTIVE) {
			return false;
		}
		
		switch(evt.getType()) {
		case ACTION_B_PERFORMED:
			GameObject sender = evt.getArgument(0);
			startSliding(sender, receiver);
			return true;
		case PUSH_COMPLETED:
			stopSliding(receiver);
			return true;
		default:
			return false;
		}
	}
	
	private void startSliding(GameObject pusherObj, GameObject pushedObj) {
		
		PhysicsComponent currPhy = pushedObj.getPhysicsComponent();
		if (currPhy != null) {
			assert(!(currPhy instanceof GridBoundPhysicsComponent));
		}
		
		GridBoundPhysicsComponent pushPhyComp = new GridBoundPhysicsComponent(pushedObj);
		pushedObj.pushPhysicsComponent(pushPhyComp);
		
		// Make the pusher object look toward the pusher and move in the opposite direction
		GameObjectUtils.lookToward(pushedObj, pusherObj);
		double moveDir = pushedObj.getFacingDirection() + 180;
		CardinalDirection moveCardDir = CardinalDirection.degree2direction(moveDir);
		pushPhyComp.move(moveCardDir);
		
//		System.out.println(moveDir + " " + moveCardDir);
	}
	
	private void stopSliding(GameObject pushedObj) {
		
//		System.out.println(pushedObj.getState());
		assert(pushedObj.getPhysicsComponent() instanceof GridBoundPhysicsComponent);
		
		pushedObj.popPhysicsComponent();
	}

}
