package pokemon_online;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.JPanel;

import pokemon_online.game.Camera;
import pokemon_online.game.Game;
import pokemon_online.game.utils.GraphicsUtils;

public class AreaGioco extends JPanel implements Serializable {

	private static final long serialVersionUID = 180210812173916644L;
	
	private static final int DEFAULT_CAMERA_WIDTH = 160; // FIXME remove all hard-coded shit
	
	private static final int DEFAULT_CAMERA_HEIGHT = 160;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

	private final Game game;
	
	private final Camera camera;

	public AreaGioco(Game game) {
		this(game, DEFAULT_CAMERA_WIDTH, DEFAULT_CAMERA_HEIGHT);
	}
	
	public AreaGioco(Game game, int cameraWidth, int cameraHeight) {
		this.game = game;
		camera = new Camera(cameraWidth, cameraHeight);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D grap = (Graphics2D) g;
		
		camera.setX(game.getPlayer().getX());
		camera.setY(game.getPlayer().getY());
		
		game.getWorld().renderWorld(camera, getBounds(), grap);
		
		// Draw game stats
//		game.drawGameStats(GraphicsUtils.translate(grap, 8,  16));
		
		// Draw HUD
		game.getHud().renderHud(grap);
	}

}
