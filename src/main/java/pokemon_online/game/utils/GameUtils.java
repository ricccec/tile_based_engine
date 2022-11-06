/**
 * 
 */
package pokemon_online.game.utils;

import java.util.Random;

import pokemon_online.Configuration;
import pokemon_online.game.GameWorld.Cell;

/**
 * @author Cecchi
 *
 */
public class GameUtils {
	
	public static final Random RANDOM = new Random();
	
	public static int getRow(int y) {
		// FIXME Use the fixed coordinate system where (0,0) is the CENTER of the top-left cell
		return (y + ((y < 1) ? 1 : 0)) / Configuration.CELL_SIZE_PXLS - ((y < 0) ? 1 : 0);
	}

	public static int getColumn(int x) {
		// FIXME Use the fixed coordinate system where (0,0) is the CENTER of the top-left cell
		return (x + ((x < 1) ? 1 : 0)) / Configuration.CELL_SIZE_PXLS - ((x < 0) ? 1 : 0);
	}
	
	public static Cell getCell(int x, int y) {
		return new Cell(getRow(y), getColumn(x));
	}

	public static int getX(int column) {
		return column *  Configuration.CELL_SIZE_PXLS;
	}

	public static int getY(int row) {
		return row *  Configuration.CELL_SIZE_PXLS;
	}
	
	public static boolean eventOccur(float eventProbability) {
		if (eventProbability == 0) {
			return false;
		}
		float rand = RANDOM.nextFloat();
		return (rand <= eventProbability);
	}
	
	/**
	 * Convert an angle expressed in radiant between -PI and +PI into its equivalent in degrees
	 * @param angRad
	 * @return
	 */
	public static double radiant2degree(double angRad) {
		double angNorm = angRad/Math.PI;
		double angDegr = ((angNorm < 0) ? 360 : 0) + 180*angNorm;
		return angDegr;
	}

}
