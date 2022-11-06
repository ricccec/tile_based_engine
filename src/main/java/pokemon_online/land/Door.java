/**
 * 
 */
package pokemon_online.land;

/**
 * @author Cecchi
 *
 */
public class Door {

	private final String destLand;
	
	private final int destRow;
	
	private final int destCol;
	
	public Door(String destinationLand, int destinationRow, int destinationCol) {
		destLand = destinationLand;
		destRow = destinationRow;
		destCol = destinationCol;
	}

	public String getDestinationLand() {
		return destLand;
	}

	public int getDestinationRow() {
		return destRow;
	}

	public int getDestinationCol() {
		return destCol;
	}
}
