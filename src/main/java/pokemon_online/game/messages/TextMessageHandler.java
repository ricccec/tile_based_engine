/**
 * 
 */
package pokemon_online.game.messages;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.messages.Message.Type;
import pokemon_online.physics.PhysicsComponent;
import pokemon_online.physics.PokemonPhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class TextMessageHandler extends MessageHandler { // FIXME Better "TextEventHandler"?
	
	private final String text;
	
	public TextMessageHandler(String text) {
		this.text = text;
	}

	@Override
	public boolean handleMessage(GameWorld world, GameObject receiver, Message msg) {
		
		switch(msg.getType()) {
			case ACTION_PERFORMED:
				GameObject sender = (GameObject)msg.getArguments().get(0);
				sendTextDisplayReqs(world, sender, receiver);
				return true;
			case HUD_DISPOSED:
				receiver.setFrozen(false);
				world.removeMessageListener(Type.HUD_DISPOSED, receiver);
				return true;
			default:
				return false;
		
		}
		
	}

	private void sendTextDisplayReqs(GameWorld world, GameObject sender, GameObject receiver) {
		
		// Handle the case of a receiver with a Physical Component
		PhysicsComponent phyComp = receiver.getPhysicsComponent();
		if (phyComp != null) {
			
			if (phyComp instanceof PokemonPhysicsComponent) { // FIXME This breaks polymorphism. Add some sort of canHandleMessage method to GameObject?
				if (((PokemonPhysicsComponent)phyComp).isCrossingCells()) {
					// Ignore message and un-freeze the sender
					sender.setFrozen(false);
					return;
				}
			}
			
			phyComp.lookToward(sender.getX(), sender.getY());
			phyComp.setFrozen(true);
		}
		
		world.sendMessage(Message.newHudDisplayText(text));
		
		// Sender and receiver waits for the HUD to be disposed
		world.addMessageListener(Type.HUD_DISPOSED, receiver);
	}

}
