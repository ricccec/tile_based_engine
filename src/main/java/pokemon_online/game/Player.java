package pokemon_online.game;

import java.util.Collection;

import pokemon_online.game.Controller.Control;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
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
		physComp = new PokemonPhysicsComponent(this);
		grapComp = new SpriteGraphicsComponent(this);
	}

	public PokemonPhysicsComponent getPhysicsComponent() {
		return (PokemonPhysicsComponent)super.getPhysicsComponent();
	}
	
	public void handleInput(GameWorld world) {
		if (getPhysicsComponent().isCrossingCells()) {
			return;
		}
		
		Controller ctrl = getController();
		if (ctrl.isStatusChanged(Control.ACTION_1) && !ctrl.isActive(Control.ACTION_1)) {
			
			getPhysicsComponent().setFrozen(true);
			
			int objRow = GameUtils.getRow(getY());
			int objCol = GameUtils.getColumn(getX());
			switch(getPhysicsComponent().getFacingDirection()) {
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
			if (!objects.isEmpty()) {
				GameObject obj = objects.iterator().next();
				obj.sendMessage(Message.newActionPerformed(this));
			}
		}
	}

}
