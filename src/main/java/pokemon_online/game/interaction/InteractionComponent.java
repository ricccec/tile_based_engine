package pokemon_online.game.interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.Component;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObjectState;
import pokemon_online.game.GameWorld;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.EventHandler;

public class InteractionComponent extends Component {

	public final Map<Control, ControlHandler> controlHandlers;
	
	private final Collection<EventHandler> msgHandlers;
	
	public InteractionComponent(GameObject obj) {
		super(obj);
		
		controlHandlers = new HashMap<>();
		msgHandlers = new ArrayList<>();
	}
	
	public void addControlHandler(Control cntrl, ControlHandler hndlr) {
		controlHandlers.put(cntrl, hndlr);
	}
	
	public void addEventHandler(EventHandler handler) {
		msgHandlers.add(handler);
	}
	
	/**
	 * This method is called by an Entity, usually inside the
	 * {@link updateInteraction} of another Entity. The method is used to handle
	 * events in the same frame where they are generated, instead of the next frame.
	 * 
	 * Return <code>true<code> if the event has been handled and doesn't need to be added to the object's queue of pending events
	 * 
	 * @param world
	 * @param msg
	 */
	public boolean notifyEvent(GameWorld world, Event msg) {
		// Pass event to handlers (if any) to process it during the current frame
		for (EventHandler handler : msgHandlers) {
			boolean handled = handler.handleEvent(world, obj, msg);
			if (handled) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called at the end of each tick of the <b>GAME LOOP</b> to process the pending events
	 * @param world
	 */
	public void updateInteraction(GameWorld world) {
		
//		System.out.println(obj.getX() + " " + obj.getY() + " " + obj.getState());
		if (//obj.getPhysicsComponent().isCrossingCells() || FIXME see below
			(obj.getState() != GameObjectState.OBJ_STATE_IDLE)) { // FIXME This means objects doesn't respond to controls except while on IDLE
			return;
		}
		
		// Handle the case of a receiver with a Physical Component
//		PhysicsComponent phyComp = obj.getPhysicsComponent();
//		if ((phyComp != null) && (phyComp instanceof PokemonPhysicsComponent)) { // FIXME This breaks polymorphism.
//			if (((PokemonPhysicsComponent)phyComp).isCrossingCells()) {
//				// Ignore message and un-freeze the sender
//				return;
//			}
//		}
		
		Controller ctrlr = obj.getController();
		
		for (Control ctrl : controlHandlers.keySet()) {
			if (ctrlr.isStatusChanged(ctrl) && ctrlr.isActive(ctrl)) {
				controlHandlers.get(ctrl).handleControl(world, obj, ctrl);
			}
		}
		
	}
	
}
