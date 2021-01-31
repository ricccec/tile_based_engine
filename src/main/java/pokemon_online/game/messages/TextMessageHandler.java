/**
 * 
 */
package pokemon_online.game.messages;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.messages.Message.Type;

/**
 * @author Cecchi
 *
 */
public class TextMessageHandler extends MessageHandler { // FIXME Better "TextEventHandler"?
	
	private final String text;
	
	public TextMessageHandler(String text) {
		super(Message.Type.ACTION_PERFORMED);
		this.text = text;
	}

	@Override
	public void handleMessage(GameWorld world, GameObject receiver, Message msg) {
		assert(msg.getType() == Type.ACTION_PERFORMED);
		world.sendMessage(Message.newHudDisplayText(text));
	}

}
