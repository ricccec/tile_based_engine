/**
 * 
 */
package pokemon_online.world_builder;

import pokemon_online.game.GameWorld.Cell;
import pokemon_online.land.Tile;

/**
 * @author Cecchi
 *
 */
public class WorldBuilder {

	private RawLand land;

	public void setLand(RawLand land) {
		this.land = land;
	}
	
	public RawLand getLand() {
		return land;
	}
	
	public void setTile(Cell cell, Componente tile) {
		land.getGrid()[cell.getRow()][cell.getColumn()] = tile;
	}
	
	public Componente getTile(Cell cell) {
		if (land.containsCell(cell)) {
			return land.getGrid()[cell.getRow()][cell.getColumn()];
		}
		return null;
	}
	public Componente removeTile(Cell cell) {
		Componente result = null;
		if (land.containsCell(cell)) {
			result = land.getGrid()[cell.getRow()][cell.getColumn()];
			land.getGrid()[cell.getRow()][cell.getColumn()] = null;
		}
		return result;
	}
	
}
