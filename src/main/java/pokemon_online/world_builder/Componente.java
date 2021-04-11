package pokemon_online.world_builder;


import java.io.Serializable;

import pokemon_online.land.Tile;

public class Componente implements Serializable{

	String testo; //Utilizzato per i cartelli

	//Utilizzate per le porte
	String landArrivo = null;
	int colonnaArrivo = 0;
	int righaArrivo = 0;

	int prezzo = 0;//Utilizzato per i check point

	private final Tile tile;

	public Componente(Tile tile) {
		this.tile = tile;
	}

	public Tile getTile() {
		return tile;
	}

	public Componente(Componente cmp) {
		if (cmp.getType() != 0) {
			throw new IllegalArgumentException("Unsupported component type");
		}
		tile = cmp.getTile();
	}
	
	public int getType() {
		if (tile != null) {
			return 0;
		} else {
			return -1; // FIXME
		}
	}
}
