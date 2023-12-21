/**
 * 
 */
package pokemon_online.game.interaction.event;

import pokemon_online.game.Game;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectState;
import pokemon_online.game.GameWorld;
import pokemon_online.hud.HudDialog;

/**
 * @author Riccardo
 *
 */
public class ScriptedEventHandler extends EventHandler {

	private final GameObject obj;
	
	public ScriptedEventHandler(GameObject obj) {
		
		this.obj = obj;
		
		Game.getEventManager().addEventListener((byte)0xfff0, obj); // FIXME Declare all custom event in a game-specifica configuration file
		Game.getEventManager().addEventListener(EventType.HUD_DISPOSED, obj);
	}
	
	@Override
	public boolean handleEvent(GameWorld world, GameObject receiver, Event evt) {
		
		assert(receiver == obj);
		
		// TODO Make this a scriptable behaviour
		if (evt.getType() == EventType.ACTION_PERFORMED) {
			GameObject sender = evt.getArgument(0);
			handleActionPerformedEvent(world, sender);
		}
		
		return true;
	}
	
	private void handleActionPerformedEvent(GameWorld world, GameObject senderObj) {
		
		if (obj.getState() != GameObjectState.OBJ_STATE_IDLE) {
			return;
		}
		
		obj.setState((byte)0xfff0); // FIXME
		senderObj.setState(GameObjectState.OBJ_STATE_TALKING); // FIXME
		Game.getHud().pushState(new HudDialog("Do you want to cut?", "YES", "NO"));
		
	}

}
