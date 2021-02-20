/**
 * 
 */
package pokemon_online.game.event;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.event.Event.Type;
import pokemon_online.physics.GridBoundPhysicsComponent;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class PushMessageHandler extends EventHandler {

	@Override
	public boolean handleEvent(GameWorld world, GameObject receiver, Event msg) {
		if (msg.getType() == Type.ACTION_B_PERFORMED) {
			PhysicsComponent currPhy = receiver.getPhysicsComponent();
			if ((currPhy == null) || !(currPhy instanceof GridBoundPhysicsComponent)) {
				GridBoundPhysicsComponent pushPhyComp = new GridBoundPhysicsComponent(receiver);
				receiver.pushPhysicsComponent(pushPhyComp);
				pushPhyComp.moveRight();
			}
			return true;
		} else {
			return false;
		}
	}

}
