/**
 * 
 */
package pokemon_online.game;

/**
 * @author Cecchi
 *
 */
public class TextMessageHandler extends MessageHandler {
	
	private final String text;
	
	public TextMessageHandler(String text) {
		super(Message.Type.ACTION_PERFORMED);
		this.text = text;
	}

	@Override
	public void handleMessage(GameWorld world, GameObject receiver, Message msg) {
		// TODO Auto-generated method stub
		
	}

}
