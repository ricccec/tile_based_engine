package pokemon_online;
import java.awt.Color;
import java.awt.Graphics2D;

import pokemon_online.game.GameWorld;
import pokemon_online.game.Keyboard;
import pokemon_online.land.Land;

/**
 * 
 */


/**
 * @author Cecchi
 *
 */
public class Game extends Thread {
 
	private final GameWorld world;

	private Player player;
	
	private long tickCount;
	
	private long lag; // Lag between real time and game time
	
	private long lastTick;
	
	private final Keyboard keyboard;
	
	public Game() {
		player = new Player();
		world = new GameWorld();
		world.spanObject(player);
		
		keyboard = new Keyboard();
		keyboard.attachController(player.getController());
	}
	
	public void jumpToLand(Land land, int playerX, int playerY) {
		world.jumpToLand(land);
		player.setPosition(playerX, playerY);
	}
	
	public void keyPressed(int keyCode) {
		keyboard.keyPressed(keyCode);
	}
	
	public void keyReleased(int keyCode) {
		keyboard.keyReleased(keyCode);
	}

	public GameWorld getWorld() {
		return world;
	}
	
	@Override
	public void run() {
		lastTick = System.currentTimeMillis();
		while(true) {
			gameLoop(System.currentTimeMillis());
		}
	}
	
	private void gameLoop(long ms) {
		
		// Check framerate
		long elapsed = ms - lastTick;
		lastTick = ms;
		lag += elapsed;

		while (lag >= Configuration.MS_PER_UPDATE) {
			world.updateWorld(Configuration.MS_PER_UPDATE);
			world.updateAnimation(Configuration.MS_PER_UPDATE);
			
			tickCount++;
			lag -= Configuration.MS_PER_UPDATE;
		}

	}
	
	public long getTickCount() {
		return tickCount;
	}

	public void setTickCount(long tickCount) {
		this.tickCount = tickCount;
	}

	public long getLag() {
		return lag;
	}

	public void setLag(long lag) {
		this.lag = lag;
	}

	public Player getPlayer() {
		return player;
	}

	public void drawGameStats(Graphics2D grap) {
		grap.setColor(Color.RED);
		grap.drawString("Player X: " + String.valueOf(getPlayer().getX()), 8, 16);
		grap.drawString("Player Y: " + String.valueOf(getPlayer().getY()), 8, 32);
		grap.drawString("Player X speed: " + String.valueOf(getPlayer().getSpeedX()), 8, 48);
		grap.drawString("Player Y speed: " + String.valueOf(getPlayer().getSpeedY()), 8, 64);
		grap.drawString("Player direction: " + String.valueOf(getPlayer().getMovingDirectionDegrees()), 8, 80);
	}
}
