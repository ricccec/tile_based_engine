/**
 * 
 */
package pokemon_online.game.ia;

import java.util.Random;

import pokemon_online.game.GameActionsState;
import pokemon_online.game.GameActionsState.GameAction;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;

/**
 * @author Cecchi
 *
 */
public class RandomIAComponent extends IAComponent {
	
	private final Random rand; // TODO Use a centralized randomizer?
	
	public RandomIAComponent(GameObject obj) {
		super(obj);
		
		rand = new Random();
	}

	@Override
	public void updateIA(GameWorld world, long dtMillisec) {
		
		int newDir = rand.nextInt(4);
		GameActionsState ctrl = obj.getController();
		ctrl.setDeactivated(GameAction.MOVE_RIGHT);
		ctrl.setDeactivated(GameAction.MOVE_DWN);
		ctrl.setDeactivated(GameAction.MOVE_LEFT);
		ctrl.setDeactivated(GameAction.MOVE_UP);
		if (newDir == 0) {
			ctrl.setActivated(GameAction.MOVE_RIGHT);
		} else if (newDir == 1) {
			ctrl.setActivated(GameAction.MOVE_DWN);
		} else if (newDir == 2) {
			ctrl.setActivated(GameAction.MOVE_LEFT);
		} else if (newDir == 3) {
			ctrl.setActivated(GameAction.MOVE_UP);
		}
		
		// TODO Add STOP and WALK state
		
//		if (obj.isMoving()) {
//			if (((obj.getX() % 32) != 0) || ((obj.getY() % 32) != 0)) { // FIXME No hard-coded shit
//				// Object is between two cells, nothing to decide
//				return;
//			}
//		} else {
//			// Change direction?
//			int newDir = rand.nextInt(4);
//			obj.setFacingDirection(90*newDir);
//		}
		
	}
	
//	public Segment getInitialSegment(GameWorld world) {
//		int objRow = world.getRow(obj.getY());
//		int objCol = world.getColumn(obj.getX());
//		Collection<Segment> segs = path.getSegmentsCrossing(objRow, objCol);
//		if (segs.isEmpty()) {
//			return null;
//		} else {
//			return segs.iterator().next();
//		}
//	}


}
