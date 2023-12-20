/**
 * 
 */
package pokemon_online;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.json.simple.parser.ParseException;

import pokemon_online.game.Game;
import pokemon_online.game.GameObject;
import pokemon_online.game.ia.AdvancedRandomIAComponent;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.interaction.InteractionComponent;
import pokemon_online.game.interaction.event.PushMessageHandler;
import pokemon_online.game.interaction.event.TextEventHandler;
import pokemon_online.game.rendering.SpriteData;
import pokemon_online.game.rendering.SpriteGraphicsComponent;
import pokemon_online.hud.HudText;
import pokemon_online.land.Land;
import pokemon_online.land.LandManager;
import pokemon_online.physics.PokemonPhysicsComponent;

/**
 * @author Cecchi
 *
 */
public class GameTester extends JFrame {

	private static final String START_LAND = "Pokecity";
	
	private static final int START_ROW = 9;
	
	private static final int START_COL = 8;
	
	public static void main(String s[]) {  
		

		Random rand = new Random(45);
		
		GameTester tester = new GameTester();		
		
		tester.setPlayerSprite("F Allenatrice");
		
		tester.jumpToLand(START_LAND, START_ROW, START_COL);

		// Spawn NPCs at random positions
		for (int i = 0; i < 128; i++) {
			GameObject gameObjNPC = new GameObject();
			
			IAComponent iaComponent = new AdvancedRandomIAComponent(gameObjNPC);
			gameObjNPC.setIAComponent(iaComponent);
			
			SpriteGraphicsComponent gComp = new SpriteGraphicsComponent(gameObjNPC);
			SpriteData gData = ResourcesManager.getMgr().getGameObjectGraphics("F Allenatrice");
			gData.initSpriteGrapComponent(gComp);
			gameObjNPC.setGraphicsComponent(gComp);
			
			PokemonPhysicsComponent phComp = new PokemonPhysicsComponent(gameObjNPC);
			gameObjNPC.setPhysicsComponent(phComp);
			
			// NPC dialogue
			String dialogue = "ddddThis is a long NPC dialogue";
			gameObjNPC.setInteractionComponent(new InteractionComponent(gameObjNPC));
			gameObjNPC.getInteractionComponent().addEventHandler(new TextEventHandler(dialogue));
			gameObjNPC.getInteractionComponent().addEventHandler(new PushMessageHandler());
			
			// Generate random position
			int landRow = (int)(rand.nextFloat()*(tester.getCurrentLand().getRowsCount() - 1));
			int landCol = (int)(rand.nextFloat()*(tester.getCurrentLand().getColsCount() - 1));
			tester.spawnObject(gameObjNPC, landRow, landCol);
		}
		
		// Spawn CUTTABLE TREE
		GameObject objTree = new GameObject();
		
		objTree.setGraphicsComponent(new SpriteGraphicsComponent(objTree));
		
		SpriteData gData = ResourcesManager.getMgr().getGameObjectGraphics("Cuttable Plant");
		gData.initSpriteGrapComponent((SpriteGraphicsComponent)objTree.getGraphicsComponent());
		
		tester.spawnObject(objTree, 9, 10);
		
		
		
		
		tester.startGameLoop();
		
		tester.setVisible(true);
		
		//tester.game.getHud().pushState(new HudText("ddddThis is a long NPC dialogue abcdefghilmnopqrstuvz"));
	}
	
	private static final long serialVersionUID = 4022568139447850178L;

	Timer timer;
	
	Game game;
	
	private Land currLand;

	private AreaGioco areaGioco;
	
	private javax.swing.JMenu jMenu1;
	
	private javax.swing.JMenuBar jMenuBar1;
	
	private javax.swing.JMenuItem jMenuItemCambiaCella;
	
	private javax.swing.JMenuItem jMenuItemCambiaLand;

	public GameTester() {

		// Init game subsistems
		Game.initGame();
		game = Game.getInstance();
		
		areaGioco = new AreaGioco(game);
		
		initComponents();
		
	}
	
	public void jumpToLand(String landName, int playerRow, int playerCol) {
		// Load land
		currLand = loadLand(landName);
		if (currLand == null) {
			throw new IllegalArgumentException("Cannot load land " + landName);
		} else {
			game.jumpToLand(currLand, playerCol, playerRow);
		}
	}
	
	public Land getCurrentLand() {
		return currLand;
	}
	
	public void startGameLoop() {
		
		startScreenRefresh();
		game.start();

		// FIXME
//		while(true) {
//			// Handle Game events
//		}
	}
	
	public void setPlayerSprite(String spriteName) {
		SpriteGraphicsComponent gComp = (SpriteGraphicsComponent)game.getPlayer().getGraphicsComponent();
		if (gComp != null) {
			SpriteData gData = ResourcesManager.getMgr().getGameObjectGraphics(spriteName);
			gData.initSpriteGrapComponent(gComp);
		}
	}
	
	public void spawnObject(GameObject obj, int row, int col) {
		game.getWorld().spanObject(obj, row, col);
		// TODO If the game is running, ensure the world state doesn't change within a tick. Use a protected section or an event system
		
		
	}
	
	private Land loadLand(String name) {
		try {
			return LandManager.getMgr().getLand(name);
		} catch (IOException | ParseException e) {
			JOptionPane.showMessageDialog(this, e.toString() , "Land loading error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private void startScreenRefresh() {
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.setRepeats(true);
		timer.setDelay((int)(1000/Configuration.FRAMERATE));
		timer.start();
	}

	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItemCambiaLand = new javax.swing.JMenuItem();
		jMenuItemCambiaCella = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Game test");
		setResizable(false);
		addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				formKeyPressed(evt);
			}

			public void keyReleased(java.awt.event.KeyEvent evt) {
				formKeyReleased(evt);
			}
		});

		areaGioco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		org.jdesktop.layout.GroupLayout areaGiocoLayout = new org.jdesktop.layout.GroupLayout(areaGioco);
		areaGioco.setLayout(areaGiocoLayout);
		areaGiocoLayout.setHorizontalGroup(areaGiocoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 348, Short.MAX_VALUE));
		areaGiocoLayout.setVerticalGroup(areaGiocoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 220, Short.MAX_VALUE));

		jMenu1.setText("Land");
		jMenuItemCambiaLand.setText("Cambia Land");
		jMenu1.add(jMenuItemCambiaLand);

		jMenuItemCambiaCella.setText("Cambia cella");
		jMenu1.add(jMenuItemCambiaCella);

		jMenuBar1.add(jMenu1);

		setJMenuBar(jMenuBar1);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, areaGioco, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(areaGioco,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
		pack();
	}

	private void formKeyReleased(java.awt.event.KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		//System.out.println(keyCode + " released");
		game.keyReleased(keyCode);
	}

	private void formKeyPressed(java.awt.event.KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		//System.out.println(keyCode + " pressed");
		game.keyPressed(keyCode);
	}

}
