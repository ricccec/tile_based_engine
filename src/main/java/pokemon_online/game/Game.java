package pokemon_online.game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Stack;

import org.apache.log4j.Logger;

import pokemon_online.Configuration;
import pokemon_online.game.messages.Message;
import pokemon_online.game.utils.GameUtils;
import pokemon_online.game.utils.GraphicsUtils;
import pokemon_online.hud.Hud;
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
	
	private final Stack<Message> msgQueue;
	
	private final GameWorld world;
	
	private final Hud hud;

	private final GameStatistics stats;
	
	private final Player player;
	
	private long lag; // Lag between real time and game time
	
	private long lastTick;
	
	private final Keyboard keyboard;
	
	public Game() {
		player = new Player();
		world = new GameWorld(this);
		hud = new Hud(this);
		stats = new GameStatistics();
		
		msgQueue = new Stack<>();
		
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
	
	public Hud getHud() {
		return hud;
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
		long elapsed = ms - lastTick;
		lastTick = ms;
		lag += elapsed;

		while (lag >= Configuration.MS_PER_UPDATE) {
			
			stats.beforeUpdate();
			
			dispatchMessages(); // Dispatch prev. frame messages
			
			world.beforeUpdate();
			
			world.updateIA(Configuration.MS_PER_UPDATE);
			world.updateControllers();
			
			world.updateWorld(Configuration.MS_PER_UPDATE);
			world.updateAnimation(Configuration.MS_PER_UPDATE);
			
			player.handleInput(world);
			
			
			hud.update(player.getController());
			
			stats.afterUpdate();
			
			lag -= Configuration.MS_PER_UPDATE;
		}

	}
	
	public void queueMessage(Message msg) {
		msgQueue.push(msg);
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
		int plyrX = getPlayer().getX();
		int plyrCol = GameUtils.getColumn(plyrX);
		int plyrY = getPlayer().getY();
		int plyrRow = GameUtils.getColumn(plyrY);
		grap.drawString("Player X: " + String.valueOf(plyrX) + "(" + String.valueOf(plyrCol) + ")", 0, 0);
		grap.drawString("Player Y: " + String.valueOf(plyrY) + "(" + String.valueOf(plyrRow) + ")", 0, 16);
		grap.drawString("Player X speed: " + String.valueOf(getPlayer().getSpeedX()), 0, 32);
		grap.drawString("Player Y speed: " + String.valueOf(getPlayer().getSpeedY()), 0, 48);
		grap.drawString("Player direction: " + String.valueOf(getPlayer().getMovingDirection()), 0, 64);
		
		stats.print(GraphicsUtils.translate(grap, 0,  80));
	}

	private void dispatchMessages() {
		while(!msgQueue.isEmpty()) {
			Message msg = msgQueue.pop();
			switch (msg.getType()) { // FIXME Move this logic inside the components
				case HUD_DISPOSED:
					player.getPhysicsComponent().setFrozen(false); // FIXME This should be responsibility of the Game world
					world.sendMessageToObjects(msg);
					break;
				case HUD_DISPLAY_TEXT:
					String msgText = msg.getArguments().iterator().next().toString();
					System.out.println("MESSAGE: " + msgText);
					hud.displayText(msgText);
					break;
				default:
				case ACTION_PERFORMED:
					break;
				
			}
		}
	}

}
