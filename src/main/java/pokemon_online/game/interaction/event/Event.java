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
		Event result = new Event(EventType.ACTION_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Event newActionBPerformed(GameObject sender) {
		Event result = new Event(EventType.ACTION_B_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Event newEventWithSender(GameObject sender, byte type) {
		Event result = new Event(type);
		result.addArgument(sender);
		return result;
	}
	
	private final byte type;
	
	private final List<Object> args;
	
	public Event(byte type) {
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

	public byte getType() {
		return type;
	}
}
