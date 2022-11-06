/**
 * 
 */
package pokemon_online.game;

import org.apache.log4j.Logger;

import pokemon_online.game.interaction.event.Event;

/**
 * @author Cecchi
 *
 */
public abstract class Component {
	
	private static final Logger LOGGER = Logger.getLogger(Component.class);

	protected final GameObject obj;
	
	public Component(GameObject obj) {
		this.obj = obj;
	}
	
	public void handleEvent(Event event) {
		LOGGER.debug(this + " has received event " + event);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + obj;
	}
	
	
}
