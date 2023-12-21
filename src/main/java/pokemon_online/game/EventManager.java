/**
 * 
 */
package pokemon_online.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.game.interaction.event.Event;

/**
 * @author Riccardo
 *
 */
/**
 * @author Riccardo
 *
 */
public class EventManager {
	
	private static final Logger LOGGER = Logger.getLogger(EventManager.class);
			
	private final Stack<Event> evtQueue;
	
	private final Map<Byte, Set<GameObject>> evtListeners;
	
	private final Game game;
	
	public EventManager(Game game) {
		this.game = game;
		
		evtListeners = new HashMap<>();
		evtQueue = new Stack<>();
	}
	
	public void addEventListener(byte evtType, GameObject listener) {
		if (!evtListeners.containsKey(evtType)) {
			evtListeners.put(evtType, new HashSet<>());
		}
		evtListeners.get(evtType).add(listener);
	}
	
	public void removeEventListener(byte evtType, GameObject listener) {
		if (evtListeners.containsKey(evtType)) {
			evtListeners.get(evtType).remove(listener);
		}
	}
	
	/**
	 * Puts an event in a queue to be fired later
	 * @param evt
	 */
	public void queueEvent(Event evt) {
		LOGGER.debug("Event received @T" + Game.getInstance().getGameStats().getCurrTickCount());
		evtQueue.push(evt);
	}
	
	/**
	 * Fire the event right now, without waiting for the loop to call {@link EventManager#update()}
	 * @param evt
	 */
	public void triggerEvent(Event evt) {
		if (evtListeners.containsKey(evt.getType())) {
			Collection<GameObject> listeners = new ArrayList<GameObject>(evtListeners.get(evt.getType())); // FIXME Create a copy 'cause during notifyEvent the listener could remove inself
			for (GameObject listener : listeners) {
				listener.notifyEvent(game.getWorld(), evt);
			}
		}
	}
	
	public void abortEvent(Event evt) {
		// Removes an event from the queue
		// TODO
	}
	
	public void update() {
		// Broadcasts the queued events to all the listeners
		while(!evtQueue.isEmpty()) {
			Event evt = evtQueue.pop();
			triggerEvent(evt);
		}
	}


}
