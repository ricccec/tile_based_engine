/**
 * 
 */
package pokemon_online.game.messages;

import java.util.ArrayList;
import java.util.List;

import pokemon_online.game.GameObject;

/**
 * @author Cecchi
 *
 */
public class Message {

	public static final Message newActionPerformed(GameObject sender) {
		Message result = new Message(Type.ACTION_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Message newHudDisplayText(String text) {
		Message result = new Message(Type.HUD_DISPLAY_TEXT);
		result.addArgument(text);
		return result;
	}
	
	public enum Type {
		ACTION_PERFORMED,
		HUD_DISPLAY_TEXT,
		HUD_DISPOSED
	}
	
	private final Type type;
	
	private final List<Object> args;
	
	public Message(Type type) {
		this.type = type;
		this.args = new ArrayList<>();
	}
	
	public void addArgument(Object arg) {
		args.add(arg);
	}
	
	public List<Object> getArguments() {
		return args;
	}

	public Type getType() {
		return type;
	}
}
