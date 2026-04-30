/**
 * 
 */
package pokemon_online.game.interaction.actions;

import java.util.ArrayList;
import java.util.List;

import pokemon_online.game.GameObject;

/**
 * @author Cecchi
 *
 */
public class Action {

	public static final Action newActionPerformed(GameObject sender) {
		Action result = new Action(Type.ACTION_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Action newActionBPerformed(GameObject sender) {
		Action result = new Action(Type.ACTION_B_PERFORMED);
		result.addArgument(sender);
		return result;
	}
	
	public static final Action newHudDisplayText(String text) {
		Action result = new Action(Type.HUD_DISPLAY_TEXT);
		result.addArgument(text);
		return result;
	}
	
	public enum Type {
		ACTION_PERFORMED,
		ACTION_B_PERFORMED,
		HUD_DISPLAY_TEXT,
		HUD_DISPOSED,
		PUSH_COMPLETED
	}
	
	private final Type type;
	
	private final List<Object> args;
	
	public Action(Type type) {
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
