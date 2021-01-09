/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Collection;

import pokemon_online.ResourcesManager;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;
import pokemon_online.land.Land;
import pokemon_online.land.Tile;
import pokemon_online.physics.PhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class GameWorld {

	private static final boolean DRAW_B_BOX = true;
	
	private final GameObjectsContainer objContainer;

	private Land currLand;

	public GameWorld() {
		objContainer = new GameObjectsContainer(this);
	}

	public void jumpToLand(Land land) {
		this.currLand = land;
		objContainer.clear();
	}

	public void spanObject(GameObject obj, int row, int col) {
		obj.setPosition(32*col, 32*row); // FIXME Remove all hard-coded shit
		objContainer.addObject(obj);
	}
	
	public Collection<GameObject> getObjects() {
		return objContainer.getAllObjects();
	}
	
	public void updateIA(long dtMillisec) {
		for (GameObject obj : getObjects()) { // FIXME Only active objects
			IAComponent iaComp = obj.getIAComponent();
			if (iaComp != null) {
				iaComp.updateIA(this, dtMillisec);
			}
		}
	}
	
	public void updateWorld(long dtMillisec) {
		for (GameObject obj : getObjects()) {
			PhysicsComponent phyComp = obj.getPhysicsComponent();
			if (phyComp != null) {
				phyComp.update(this, dtMillisec);
			}
		}
	}
	
	public void updateAnimation(long dtMillisec) {
		for (GameObject obj : getObjects()) {
			// FIXME Only update active objects
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.updateAnimation(dtMillisec);
			}
		}
	}

	public void updateControllers() {
		for (GameObject obj : getObjects()) {
			// FIXME Only update active objects
			Controller controller = obj.getController();
			if (controller != null) {
				controller.updateController();
			}
		}
	}

	public void renderWorld(Camera camera, Rectangle bounds, Graphics2D grap) {
		
		int componentWidth = bounds.width;
		int componentHeight = bounds.height;
		
		// Cover previous frame
		grap.setColor(Color.BLACK);
		grap.fillRect(0, 0, 1000, 1000);

		if (currLand == null)
			return;

		// Compute camera center coords in screen space
		int plyrScreenX = componentWidth / 2 - 16;
		int plyrScreenY = componentHeight / 2 - 16;

		// Calculate the camera's bounding box in world's space
		// FIXME Camera is always centered at the player: make the camera movable
		int xMin = camera.getX() - (int) (32 * Math.floor((camera.getWidth() / 2) / 32f));
		int xMax = xMin + camera.getWidth();
		int yMin = camera.getY() - (int) (32 * Math.floor((camera.getHeight() / 2) / 32f));
		int yMax = yMin + camera.getHeight();

		// System.out.println("Screen rect: (" + xMin + "," + yMin + "),(" + xMax + ","
		// + yMax + ")"); // FIXME use a fucking logger;

		// Bleed the box to whole cells
		int colMin = xMin / 32; // FIXME no hardcoded shit
		int colMax = (xMax - 1) / 32;
		int rowMin = yMin / 32;
		int rowMax = (yMax - 1) / 32;

		// System.out.println("Screen rect (bleed): (" + colMin + "," + rowMin + "),(" +
		// colMax + "," + rowMax + ")"); // FIXME use a fucking logger;

		// Land origin in screen space
		int landOriginX = plyrScreenX - camera.getX();
		int landOriginY = plyrScreenY - camera.getY();

		// Draw tile's ground sprites
		for (int c = colMin; c <= colMax; c++) {// Per ogni colonna
			for (int r = rowMin; r <= rowMax; r++) {// Per ogni righa

				// Cell coordinates in screen space
				int screenX = landOriginX + 32 * c;
				int screeny = landOriginY + 32 * r;

				Tile tile = currLand.getCellTile(r, c);
				if (tile != null) {
					Image tileImg = ResourcesManager.getMgr().getImage(tile.getImage());
					grap.drawImage(tileImg, screenX, screeny, null);
				}
			}
		}

		// Draw viewport rectangle
		grap.setColor(Color.RED);
		grap.drawRect(componentWidth / 2 - 16 - 64, componentHeight / 2 - 16 - 64, 160, 160);

		// Draw game objects
		Viewport viewport = new Viewport(rowMin, rowMax, colMin, colMax, landOriginX, landOriginY);
		for (GameObject obj : objContainer.getSortedObjectsInRange(rowMin - 1, colMin - 1, rowMax, colMax)) {
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.render(grap, viewport);
			}
		}
	}

	public boolean isWalkable(int row, int col) {
		return currLand.isWalkable(row, col);
	}
	
	public static class Cell {

		private final int row;
		
		private final int col;
		
		public Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public int getRow() {
			return row;
		}

		public int getColumn() {
			return col;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + col;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (col != other.col)
				return false;
			if (row != other.row)
				return false;
			return true;
		}
	}

	public Collection<GameObject> getProps(int row, int col) {
		return objContainer.getProps(row, col);
	}

}
