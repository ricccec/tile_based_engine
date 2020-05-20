package pokemon_online;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.JPanel;

import pokemon_online.game.Camera;

public class AreaGioco extends JPanel implements Serializable {

	private static final long serialVersionUID = 180210812173916644L;
	
	private static final int CAMERA_WIDTH = 160; // FIXME remove all hard-coded shit
	
	private static final int CAMERA_HEIGHT = 160;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

	private final Game game;
	
	private final Camera camera;

	public AreaGioco(Game game) {
		this.game = game;
		camera = new Camera(CAMERA_WIDTH, CAMERA_HEIGHT);
	}

	/**
	 * Holds value of property xLand.
	 */
	private int XLand = 0;
	private int YLand = 0;

	public int getXLand() {
		return this.XLand;
	}

	public void setXLand(int XLand) {
		this.XLand = XLand;
	}

	public int getYLand() {
		return this.YLand;
	}

	public void setYLand(int YLand) {
		this.YLand = YLand;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D grap = (Graphics2D) g;
		
		camera.setX(game.getPlayer().getX());
		camera.setY(game.getPlayer().getY());
		
		game.getWorld().renderWorld(camera, getWidth(), getHeight(), grap);
		
		// Draw game stats
		game.drawGameStats(grap);
	}

}
