/**
 * 
 */
package pokemon_online.world_builder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import pokemon_online.ResourcesManager;
import pokemon_online.game.GameWorld.Cell;
import pokemon_online.land.Land;
import pokemon_online.land.Tile;
import pokemon_online.land.TileImage;

/**
 * @author Cecchi
 *
 */
public class RawLand {

	private final String name;

	private final Componente[][] grid;

	public RawLand(String name, int rowCount, int colCount) {
		this.name = name;
		grid = new Componente[rowCount][colCount];
	}

	public RawLand(Land land) {
		this(land.getName(), land.getRowsCount(), land.getColsCount());

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				Tile tile = land.getCellTile(row, col);
				if (tile != null) {
					grid[row][col] = new Componente(tile);
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public Componente[][] getGrid() {
		return grid;
	}

	public boolean containsCell(Cell cell) {
		return ((cell.getRow() >= 0) && (cell.getRow() < getRowsCount()) && (cell.getColumn() >= 0)
				&& (cell.getColumn() < getColsCount()));
	}

	public int getColsCount() {
		return grid[0].length;
	}

	public int getRowsCount() {
		return grid.length;
	}

	public void paint(Graphics2D grap, Rectangle bounds, float scaleFactor) {
		
        if ((bounds.height == 0) || (bounds.width == 0)) {
        	return;
        }

        //Disegno il rettangolo grigio di sfondo
        grap.setColor(Color.GRAY);
        int visibleAreaWidth = bounds.width + bounds.x;
        int visibleAreaHeight = bounds.height + bounds.y;
        grap.fillRect(0, 0, visibleAreaWidth, visibleAreaHeight);
        
        //Disegno il rettangolo bianco
        grap.setColor(Color.WHITE);
        int landWidthPxl = 32*getColsCount();
        int landHeightPxl = 32*getRowsCount();
        grap.fillRect(Math.max(0, bounds.x), Math.max(0, bounds.y), (Math.min(landWidthPxl, bounds.width) + Math.min(0, bounds.x)), (Math.min(landHeightPxl, bounds.height) + Math.min(0, bounds.y)));
        
        //Disegno la griglia
        grap.setColor(Color.GRAY);
        for (int c = 0; c < getColsCount(); c++){
            for (int r = 0; r < getRowsCount(); r++){
                grap.drawRect(c * 32 + bounds.x, r * 32 + bounds.y, 32, 32);
            }
        }
            
        //Disegno i componenti
        for (int c = 0; c < getColsCount(); c++){//Per ogni colonna
            for (int r = 0; r < getRowsCount(); r++){//Per ogni righa
            	Componente comp = grid[r][c];
                if ((comp != null) && (comp.getTile() != null)){
                	TileImage tileImg = comp.getTile().getImage(0); // FIXME No fixed frame
                	Image img = ResourcesManager.getMgr().getTileImage(tileImg);
                    grap.drawImage(img, c * 32 + bounds.x, r * 32 + bounds.y, null);
                }
            }
        }
//            
//            //Disegno il componente in uso alle coordinate del mouse
//            if (this.componenteInUso != null){
//            	TileImage tileImg = componenteInUso.getTile().getImage(0);
//            	Image img = ResourcesManager.getMgr().getTileImage(tileImg);
//                grap.drawImage(img, xMouse - 16, yMouse - 16, this);
//            }
        
	}
}
