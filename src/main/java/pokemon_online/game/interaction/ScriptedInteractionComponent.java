/**
 * 
 */
package pokemon_online.game.interaction;

import java.util.Deque;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.Event.Type;

/**
 * @author Riccardo
 *
 */
public class ScriptedInteractionComponent extends InteractionComponent {

	public ScriptedInteractionComponent(GameObject obj) {
		super(obj);
	}

	public void updateInteraction(GameWorld world) {
		// TODO Make this a scriptable behaviour

		// pick and handle the first action performed event. Ignore the rest.
		Deque<Event> evtQueue = this.getGameObject().getPendingEventsQueue();

		// Since upon finishing this method empties the queue of all events generated
		// during the current frame, at this point there should be no event generated
		// during the PREVIOUS frame
		assert (evtQueue.pollFirst() == GameObject.EVT_QUEUE_END);
		
		Event currEvent = null;
		while(!evtQueue.isEmpty()) {
			currEvent = evtQueue.pollFirst();
			if (currEvent.getType() == Type.ACTION_PERFORMED) {
				
				break;
			}
		}
		
		evtQueue.clear();
		evtQueue.add(GameObject.EVT_QUEUE_END);
	}
}
