/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;

import pokemon_online.GameObject;
import pokemon_online.ResourcesManager;
import pokemon_online.game.rendering.Viewport;
import pokemon_online.land.Land;
import pokemon_online.land.Tile;

/**
 * @author Cecchi
 *
 */
public class GameWorld {

	private final Collection<GameObject> gameObjects;

	private Land currLand;

	public GameWorld() {
		this.gameObjects = new ArrayList<>();
	}

	public void jumpToLand(Land land) {
		this.currLand = land;
	}

	public void spanObject(GameObject obj) {
		gameObjects.add(obj);
	}
	
	public void updateWorld(long dtMillisec) {
		for (GameObject obj : gameObjects) {
			PhysicsComponent phyComp = obj.getPhysicsComponent();
			if (phyComp != null) {
				phyComp.update(this, dtMillisec);
			}
		}
	}

	public void renderWorld(Camera camera, int width, int height, Graphics2D grap) {

		// Cover previous frame
		grap.setColor(Color.BLACK);
		grap.fillRect(0, 0, 1000, 1000);

		if (currLand == null)
			return;

		// Compute camera center coords in screen space
		int plyrScreenX = width / 2 - 16;
		int plyrScreenY = height / 2 - 16;

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
					Image tileImg = ResourcesManager.getMgr().getTileImage(tile.getImage());
					grap.drawImage(tileImg, screenX, screeny, null);
				}
			}
		}

		// Draw viewport rectangle
		grap.setColor(Color.RED);
		grap.drawRect(width / 2 - 16 - 64, height / 2 - 16 - 64, 160, 160);

		// Draw game objects
		Viewport viewport = new Viewport(rowMin, rowMax, colMin, colMax, landOriginX, landOriginY);
		for (GameObject obj : gameObjects) {
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.render(grap, viewport);
			}
		}
	}

	public boolean isWalkable(int row, int col) {
		return currLand.isWalkable(row, col);
	}

	public int getRow(int y) {
		return y / 32 - ((y < 0) ? 1 : 0);
	}

	public int getColumn(int x) {
		return x / 32 - ((x < 0) ? 1 : 0);
	}

	public int getX(int column) {
		return column * 32;
	}

	public int getY(int row) {
		return row * 32;
	}

	public void updateAnimation(long dtMillisec) {
		for (GameObject obj : gameObjects) {
			// FIXME Only update active objects
			GraphicsComponent gComp = obj.getGraphicsComponent();
			if (gComp != null) {
				gComp.updateAnimation(0);
			}
		}
	}

	public void updateControllers() {
		for (GameObject obj : gameObjects) {
			// FIXME Only update active objects
			Controller controller = obj.getController();
			if (controller != null) {
				controller.updateController();
			}
		}
	}

}
