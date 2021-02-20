package pokemon_online.game.interaction;

import java.util.HashMap;
import java.util.Map;

import pokemon_online.game.Component;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameObject.State;
import pokemon_online.physics.PhysicsComponent;
import pokemon_online.physics.PokemonPhysicsComponent;
import pokemon_online.game.GameWorld;

public class InteractionComponent extends Component {

	public final Map<Control, ControlHandler> controlHandlers;
	
	public InteractionComponent(GameObject obj) {
		super(obj);
		
		controlHandlers = new HashMap<>();
	}
	
	public void addControlHandler(Control cntrl, ControlHandler hndlr) {
		controlHandlers.put(cntrl, hndlr);
	}

	public void updateInteraction(GameWorld world) {
		
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
