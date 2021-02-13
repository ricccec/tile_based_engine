package pokemon_online.game;

import java.util.Collection;

import pokemon_online.game.Controller.Control;
import pokemon_online.game.messages.Message;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.physics.PokemonPhysicsComponent;
/**
 * 
 */

/**
 * @author Cecchi
 *
 */
public class Player extends GameObject {
	
	public Player() {
		setPhysicsComponent(new PokemonPhysicsComponent(this));
		grapComp = new SpriteGraphicsComponent(this);
	}

	public PokemonPhysicsComponent getPhysicsComponent() {
		return (PokemonPhysicsComponent)super.getPhysicsComponent();
	}
	
	public void handleInput(GameWorld world) { // TODO Chose a better name
		if (getPhysicsComponent().isCrossingCells() ||
			getPhysicsComponent().isFrozen()) {
			return;
		}
		
		Controller ctrl = getController();
		
		// Get the object the action has been performed onto
		int objRow = GameUtils.getRow(getY());
		int objCol = GameUtils.getColumn(getX());
		switch(GameObjectUtils.getCardinalFacingDir(this)) { // FIXME Move this logic somewhere else
			case DIR_DOWN:
				objRow++;
				break;
			case DIR_LEFT:
				objCol--;
				break;
			case DIR_RIGHT:
				objCol++;
				break;
			case DIR_UP:
				objRow--;
				break;
			default:
				break;
		}
		Collection<GameObject> objects = world.getObjects(objRow, objCol);
		if (objects.isEmpty()) {
			return;
		}
		
		// FIXME  
		if (ctrl.isStatusChanged(Control.ACTION_1) && ctrl.isActive(Control.ACTION_1)) {
			
			// Send message
			// Froze the player
			getPhysicsComponent().setFrozen(true);
			
			GameObject obj = objects.iterator().next();
			obj.sendMessage(world, Message.newActionPerformed(this));
		}
		
		if (ctrl.isStatusChanged(Control.ACTION_2) && ctrl.isActive(Control.ACTION_2)) {
			GameObject obj = objects.iterator().next();
			obj.sendMessage(world, Message.newActionBPerformed(this));
		}
	}

}
