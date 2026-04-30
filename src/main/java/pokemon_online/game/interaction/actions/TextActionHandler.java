/**
 * 
 */
package pokemon_online.game.interaction.actions;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.interaction.actions.Action.Type;
import pokemon_online.game.GameWorld;
import pokemon_online.game.utils.GameObjectUtils;

/**
 * @author Cecchi
 *
 */
public class TextActionHandler extends ActionHandler { // FIXME Better "TextEventHandler"?
	
	private final String text;
	
	public TextActionHandler(String text) {
		this.text = text;
	}

	@Override
	public boolean handleAction(GameWorld world, GameObject receiver, Action evt) {
		
		switch(evt.getType()) {
			case ACTION_PERFORMED:
				GameObject sender = (GameObject)evt.getArguments().get(0);
				if (receiver.getState() == State.ACTIVE) {
					sendHudTextDisplayReqs(world, sender, receiver);
					return true;
				}
			case HUD_DISPOSED: // Unlock the object who is talking
				assert(receiver.getState() != State.ACTIVE);
				receiver.setState(State.ACTIVE);
				world.removeMessageListener(Type.HUD_DISPOSED, receiver);
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
		sender.setState(State.FROZEN);
		receiver.setState(State.FROZEN); // Ignore any other events
		world.sendMessage(Action.newHudDisplayText(text));
		
		// Sender and receiver waits for the HUD to be disposed
		world.addMessageListener(Type.HUD_DISPOSED, receiver);
		world.addMessageListener(Type.HUD_DISPOSED, sender);
		
	}

}
