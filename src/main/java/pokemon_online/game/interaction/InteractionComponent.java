package pokemon_online.game.interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.Component;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.EventHandler;
import pokemon_online.physics.PhysicsComponent;
import pokemon_online.physics.PokemonPhysicsComponent;
import pokemon_online.game.GameWorld;

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
	 * @param world
	 * @param msg
	 */
	public void notifyEvent(GameWorld world, Event msg) {
		// Pass event to handlers (if any) to process it during the current frame
		for (EventHandler handler : msgHandlers) {
			handler.handleEvent(world, obj, msg);
		}
	}

	public void updateInteraction(GameWorld world) {
		
//		System.out.println(obj.getX() + " " + obj.getY() + " " + obj.getState());
		if (//obj.getPhysicsComponent().isCrossingCells() || FIXME see below
			(obj.getState() != State.ACTIVE)) {
			return;
		}
		
		// Handle the case of a receiver with a Physical Component
		PhysicsComponent phyComp = obj.getPhysicsComponent();
		if ((phyComp != null) && (phyComp instanceof PokemonPhysicsComponent)) { // FIXME This breaks polymorphism.
			if (((PokemonPhysicsComponent)phyComp).isCrossingCells()) {
				// Ignore message and un-freeze the sender
				return;
			}
		}
		
		Controller ctrlr = obj.getController();
		
		for (Control ctrl : controlHandlers.keySet()) {
			if (ctrlr.isStatusChanged(ctrl) && ctrlr.isActive(ctrl)) {
				controlHandlers.get(ctrl).handleControl(world, obj, ctrl);
			}
		}
		
	}
	
}
