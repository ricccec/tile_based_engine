/**
 * 
 */
package pokemon_online.game;

/**
 * @author Cecchi
 *
 */
public abstract class MessageHandler {

	private final Message.Type msgType;
	
	public MessageHandler(Message.Type msgType) {
		this.msgType = msgType;
	}
	
	public Message.Type getMsgType() {
		return msgType;
	}
	
	public abstract void handleMessage(GameWorld world, GameObject receiver, Message msg);
	
}
