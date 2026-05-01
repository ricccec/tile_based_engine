/**
 * 
 */
package pokemon_online.game.interaction.interactions;

import java.util.ArrayList;
import java.util.List;

import pokemon_online.game.GameObject;

/**
 * Represents one {@link GameObject} performing an interaction onto another — push, dialogue trigger, action button press, etc. 
 * @author Cecchi
 *
 */
public class Interaction {

	public static final Interaction newActionAPerformed(GameObject sender) {
		Interaction result = new Interaction(Type.ACTION_A_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Interaction newActionBPerformed(GameObject sender) {
		Interaction result = new Interaction(Type.ACTION_B_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Interaction newHudDisplayText(String text) {
		Interaction result = new Interaction(Type.HUD_DISPLAY_TEXT);
		result.addArgument(text);
		return result;
	}
	
	public enum Type {
		ACTION_A_PERFORMED,
		ACTION_B_PERFORMED,
		HUD_DISPLAY_TEXT,
		HUD_DISPOSED,
		PUSH_COMPLETED
	}
	
	private final Type type;
	
	private final List<Object> args;
	
	public Interaction(Type type) {
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
