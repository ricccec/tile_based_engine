/**
 * 
 */
package pokemon_online.game.interaction.event;

import java.util.ArrayList;
import java.util.List;

import pokemon_online.game.GameObject;

/**
 * @author Cecchi
 *
 */
public class Event {

	public static final Event newActionPerformed(GameObject sender) {
		Event result = new Event(Type.ACTION_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Event newActionBPerformed(GameObject sender) {
		Event result = new Event(Type.ACTION_B_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Event newEventWithSender(GameObject sender, Type type) {
		Event result = new Event(type);
		result.addArgument(sender);
		return result;
	}
	
	public static final Event newHudDisplayText(String text) {
		Event result = new Event(Type.HUD_DISPLAY_TEXT);
		result.addArgument(text);
		return result;
	}
	
	public enum Type {
		ACTION_PERFORMED,
		ACTION_B_PERFORMED,
		HUD_DISPLAY_TEXT,
		HUD_DISPOSED,
		PUSH_COMPLETED,
		
		ZONE_ENTERING,
		ZONE_EXITING
	}
	
	private final Type type;
	
	private final List<Object> args;
	
	public Event(Type type) {
		this.type = type;
		this.args = new ArrayList<>();
	}
	
	public void addArgument(Object arg) {
		args.add(arg);
	}
	
	public List<Object> getArguments() {
		return args;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getArgument(int indx) {
		List<Object> args = getArguments();
		return (T)args.get(indx);
	}

	public Type getType() {
		return type;
	}
}
