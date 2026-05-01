/**
 * 
 */
package pokemon_online.game.interaction;

import pokemon_online.game.GameActionsState.GameAction;
import pokemon_online.game.utils.GameObjectUtils;
import pokemon_online.game.utils.GameUtils;

import java.util.Collection;

import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public abstract class GameActionHandler {

	public abstract boolean handleAction(GameWorld world, GameObject controlled, GameAction action);
	
	protected Collection<GameObject> getActionTargets(GameWorld world, GameObject obj) {
		// Get the object the action has been performed onto
		int objRow = GameUtils.getRow(obj.getY());
		int objCol = GameUtils.getColumn(obj.getX());
		switch(GameObjectUtils.getCardinalFacingDir(obj)) { // FIXME Move this logic somewhere else
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
		return world.getObjects(objRow, objCol);
	}
}
