package pokemon_online.game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.Configuration;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.game.utils.GraphicsUtils;
import pokemon_online.hud.Hud;
import pokemon_online.hud.HudText;
import pokemon_online.land.Land;

/**
 * 
 */


/**
 * @author Cecchi
 *
 */
public class Game extends Thread {
 
	private static final Logger LOGGER = Logger.getLogger(Game.class);
	
	public static final void initGame() {
		gameInstance = new Game();
	}
	
	public static final Game getInstance() {
		return gameInstance;
	}
	
	// *************************
	// Subsystems access methods
	// *************************
	
	public static final EventManager getEventManager() {
		return gameInstance.evtMgr;
	}
	
	public static final Hud getHud() {
		return gameInstance.hud;
	}
	
	private static Game gameInstance;
	
	private final Stack<GameState> stateStack;
	
	private final GameWorld world;
	
	private final Hud hud;
	
	private final EventManager evtMgr;

	private final GameStatistics stats;
	
	private final Player player;
	
	private long lag; // Lag between real time and game time
	
	private long lastTick;
	
	private final Keyboard keyboard;
	
	private Game() {
		player = new Player();
		world = new GameWorld();
		hud = new Hud(this);
		evtMgr = new EventManager(this);
		stats = new GameStatistics();
		
		stateStack  = new Stack<>();
		
		keyboard = new Keyboard();
		keyboard.attachController(player.getController());
	}
	
	public void jumpToLand(Land land, int playerRow, int playerCol) {
		
		world.jumpToLand(land);
		world.spanObject(player, playerCol, playerRow);

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
		
		LOGGER.debug("Game started");
		
		lastTick = System.currentTimeMillis();
		while(true) {
			gameLoop(System.currentTimeMillis());
		}
	}
	
	private void gameLoop(long ms) {
		
		// Check framerate
		long elapsed = ms - lastTick; // Millisecons elapsed from last tick
		lastTick = ms;
		lag += elapsed;

		while (lag >= Configuration.MS_PER_UPDATE) {
			
			stats.beforeUpdate();
			
			evtMgr.update(); // Dispatch the events generated during the prev. frame to the game objects that listen to them
			
			world.beforeUpdate();
			
			world.updateIA(Configuration.MS_PER_UPDATE);
			world.updateControllers();
			
			world.updateWorld(Configuration.MS_PER_UPDATE);
			
			world.updateAnimation(Configuration.MS_PER_UPDATE);
			
			player.getInteractionComponent().updateInteraction(world); // FIXME make all objects interact
			
			// Update HUD
			hud.update(Configuration.MS_PER_UPDATE, player.getController());
			
			stats.afterUpdate(Configuration.MS_PER_UPDATE);
			
			lag -= Configuration.MS_PER_UPDATE;
		}

	}
	

	public Player getPlayer() {
		return player;
	}
	
	public GameStatistics getGameStats() {
		return stats;
	}

	public void drawGameStats(Graphics2D grap) {
		grap.setColor(Color.RED);
		int plyrX = getPlayer().getX();
		int plyrCol = GameUtils.getColumn(plyrX);
		int plyrY = getPlayer().getY();
		int plyrRow = GameUtils.getColumn(plyrY);
		grap.drawString("Player X: " + String.valueOf(plyrX) + "(c: " + String.valueOf(plyrCol) + ")", 0, 0);
		grap.drawString("Player Y: " + String.valueOf(plyrY) + "(r: " + String.valueOf(plyrRow) + ")", 0, 16);
		grap.drawString("Player X speed: " + String.valueOf(getPlayer().getPhysicsComponent().getSpeedX()), 0, 32);
		grap.drawString("Player Y speed: " + String.valueOf(getPlayer().getPhysicsComponent().getSpeedY()), 0, 48);
		grap.drawString("Player direction: " + String.valueOf(getPlayer().getPhysicsComponent().getMovingDirection()), 0, 64);
		
		stats.print(GraphicsUtils.translate(grap, 0,  80));
	}

	

}
