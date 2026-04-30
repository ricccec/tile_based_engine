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
import pokemon_online.game.interaction.actions.Action;
import pokemon_online.game.interaction.actions.ActionHandler;
import pokemon_online.physics.PhysicsComponent;
import pokemon_online.physics.PokemonPhysicsComponent;
import pokemon_online.game.GameWorld;

/**
 * {@link Component} responsible for input handling and peer-to-peer event
 * dispatch on a {@link GameObject}.
 *
 * <p>Two registries are maintained:
 * <ul>
 *   <li><b>controlHandlers</b> — keyed on {@link Control}; polled every tick by
 *       {@link #updateInteraction}. A handler fires when its control transitions
 *       to the active state (edge-triggered, not level-triggered).</li>
 *   <li><b>msgHandlers</b> — invoked immediately when another object calls
 *       {@link #notifyEvent}; used for same-frame event processing (e.g. push,
 *       dialogue, door triggers).</li>
 * </ul>
 *
 * <p><b>Migration note:</b> This class is scheduled to become an
 * {@code EntityComponent} and {@code GameObject} will be replaced by
 * {@code Entity} in Phase 0.4 of the refactoring plan.
 */
public class InteractionComponent extends Component {

	public final Map<Control, ControlHandler> controlHandlers;
	
	private final Collection<ActionHandler> actionHandlers;
	
	/**
	 * Constructs an {@code InteractionComponent} for the given owner.
	 *
	 * @param obj the {@link GameObject} that owns this component
	 */
	public InteractionComponent(GameObject obj) {
		super(obj);
		
		controlHandlers = new HashMap<>();
		actionHandlers = new ArrayList<>();
	}
	
	/**
	 * Registers a handler for the given {@link Control}.
	 * Replaces any previously registered handler for the same control.
	 *
	 * @param cntrl the control input to listen for
	 * @param hndlr the handler to invoke on a rising-edge activation
	 */
	public void addControlHandler(Control cntrl, ControlHandler hndlr) {
		controlHandlers.put(cntrl, hndlr);
	}
	
	/**
	 * Registers an {@link ActionHandler} to be called when {@link #notifyEvent}
	 * is invoked on this component. Multiple handlers are supported and are
	 * invoked in insertion order.
	 *
	 * @param handler the event handler to register
	 */
	public void addActionHandler(ActionHandler handler) {
		actionHandlers.add(handler);
	}
	
	/**
	 * Delivers an {@link Action} to this component's registered event handlers
	 * <em>synchronously</em>, within the same frame the event is generated.
	 *
	 * <p>This is a direct, frame-synchronous call path. It is used for interactions
	 * between adjacent objects (e.g. push, dialogue trigger, door activation) where
	 * the originating object calls into its neighbour's component directly rather
	 * than deferring through the future {@code EventBus}.
	 *
	 * @param world the current {@link GameWorld}
	 * @param action   the {@link Action} to deliver
	 */
	public void notifyAction(GameWorld world, Action action) {
		// Pass event to handlers (if any) to process it during the current frame
		for (ActionHandler handler : actionHandlers) {
			handler.handleAction(world, obj, action);
		}
	}

	/**
	 * Per-tick update: polls the owner's {@link Controller} for active controls
	 * and fires any matching {@link ControlHandler} on a rising edge (i.e. only
	 * when a control's status has changed <em>and</em> is currently active).
	 *
	 * <p>The update is skipped when:
	 * <ul>
	 *   <li>the owner is not in {@link State#ACTIVE};</li>
	 *   <li>the owner has a {@link PokemonPhysicsComponent} that is mid-cell
	 *       (crossing cells), to prevent input during movement transitions.</li>
	 * </ul>
	 *
	 * @param world the current {@link GameWorld}
	 */
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
