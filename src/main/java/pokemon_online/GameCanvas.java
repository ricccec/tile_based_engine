package pokemon_online;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import pokemon_online.game.Camera;
import pokemon_online.game.Game;

public class GameCanvas extends Canvas {

	private static final int DEFAULT_CAMERA_WIDTH = 160; // FIXME remove all hard-coded shit

	private static final int DEFAULT_CAMERA_HEIGHT = 160;

	private final Game game;

	private final Camera camera;

	private volatile boolean running = false;

	private Thread renderThread;

	public GameCanvas(Game game) {
		this(game, DEFAULT_CAMERA_WIDTH, DEFAULT_CAMERA_HEIGHT);
	}

	public GameCanvas(Game game, int cameraWidth, int cameraHeight) {
		this.game = game;
		camera = new Camera(cameraWidth, cameraHeight);

	}

	public void startRenderLoop() {
		running = true;

		if (renderThread == null) {
			createBufferStrategy(2);

			renderThread = new Thread(this::renderLoop, "RenderThread");
			renderThread.setDaemon(true);
			renderThread.start();
		}

	}

	public void stopRenderLoop() {
		running = false;
	}

	private void renderLoop() {
		final long msPerFrame = 1000 / Configuration.FRAMERATE;
		while (running) {
			long start = System.currentTimeMillis();
			renderFrame();
			long elapsed = System.currentTimeMillis() - start;
			long sleep = msPerFrame - elapsed;
			if (sleep > 0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
	}

	private void renderFrame() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			return;
		}
		do {
			do {
				Graphics2D grap = (Graphics2D) bs.getDrawGraphics();
				try {
					camera.setX(game.getPlayer().getX());
					camera.setY(game.getPlayer().getY());
					game.getWorld().renderWorld(camera, getBounds(), grap);
					game.getHud().renderHud(grap);
				} finally {
					grap.dispose();
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

}
