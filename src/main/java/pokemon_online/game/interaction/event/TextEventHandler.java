/**
 * 
 */
package pokemon_online.game.interaction.event;

import pokemon_online.game.Game;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.GameWorld;
import pokemon_online.game.interaction.event.Event.Type;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.hud.HudText;

/**
 * @author Cecchi
 *
 */
public class TextEventHandler extends EventHandler {
	
	private final String text;
	
	public TextEventHandler(String text) {
		this.text = text;
	}

	@Override
	public boolean handleEvent(GameWorld world, GameObject receiver, Event evt) {
		
		switch(evt.getType()) {
			case ACTION_PERFORMED:
				GameObject sender = (GameObject)evt.getArguments().get(0);
				if (receiver.getState() == State.OBJ_STATE_IDLE) {
//					System.out.println("HUD request");
					sendHudTextDisplayReqs(world, sender, receiver);
					return true;
				} else {
					return false;
				}
			case HUD_DISPOSED: // Unlock the object who is talking
//				System.out.println("Disposed " + receiver);
				assert(receiver.getState() != State.OBJ_STATE_IDLE);
				receiver.setState(State.OBJ_STATE_IDLE);
				Game.getEventManager().removeEventListener(Type.HUD_DISPOSED, receiver);
				return true;
			default:
				return false;
		
		}
		
	}

	/**
	 * Send a GUI request to display the text
	 * @param world
	 * @param sender
	 * @param receiver
	 */
	private void sendHudTextDisplayReqs(GameWorld world, GameObject sender, GameObject receiver) {
		
//		// Handle the case of a receiver with a Physical Component
//		PhysicsComponent phyComp = receiver.getPhysicsComponent();
//		if ((phyComp != null) && (phyComp instanceof PokemonPhysicsComponent)) { // FIXME This breaks polymorphism. Add some sort of canHandleMessage method to GameObject?
//			System.out.println(receiver.getX() + " " + receiver.getY());
//			if (((PokemonPhysicsComponent)phyComp).isCrossingCells()) {
//				// Ignore message and un-freeze the sender
//				return;
//			}
//		}
		
		GameObjectUtils.lookToward(receiver, sender.getX(), sender.getY());
		sender.setState(State.OBJ_STATE_MOVING);
		receiver.setState(State.OBJ_STATE_MOVING); // Ignore any other events
		Game.getHud().pushState(new HudText(text));
		
		// Sender and receiver waits for the HUD to be disposed
		Game.getEventManager().addEventListener(Type.HUD_DISPOSED, receiver);
		Game.getEventManager().addEventListener(Type.HUD_DISPOSED, sender);
		
	}

}
