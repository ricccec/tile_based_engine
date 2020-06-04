/**
 * 
 */
package pokemon_online;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.json.simple.parser.ParseException;

import pokemon_online.game.GraphicsComponent;
import pokemon_online.game.rendering.ObjectGraphicsData;
import pokemon_online.land.Land;
import pokemon_online.land.LandManager;

/**
 * @author Cecchi
 *
 */
public class GameTester extends JFrame {

	private static final long serialVersionUID = 4022568139447850178L;

	Land[] elencoLand;

	Timer timer;
	Game game;

	private AreaGioco areaGioco;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItemCambiaCella;
	private javax.swing.JMenuItem jMenuItemCambiaLand;

	public GameTester() {

		game = new Game();
		areaGioco = new AreaGioco(game);
		initComponents();
		
	}
	
	public void setPlayerSprite(String spriteName) {
		GraphicsComponent gComp = game.getPlayer().getGraphicsComponent();
		if (gComp != null) {
			ObjectGraphicsData gData = ResourcesManager.getMgr().getGameObjectGraphics(spriteName);
			gData.setGraphics(gComp);
		}
	}
	
	public void spawnObject(GameObject obj, int row, int col) {
		game.getWorld().spanObject(obj, row, col);
	}
	
	public void startGame(String startLand, int startRow, int startCol) {
		
		// Load land
		Land land = loadLand(startLand);
		
		if (land != null) {
			game.jumpToLand(land, startCol, startRow);
			startScreenRefresh();
			game.start();
		}
		
		// FIXME
//		while(true) {
//			// Handle Game events
//		}
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
	}// </editor-fold>//GEN-END:initComponents

	private void formKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_formKeyReleased
		int keyCode = evt.getKeyCode();
		//System.out.println(keyCode + " released");
		game.keyReleased(keyCode);
	}

	private void formKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_formKeyPressed
		int keyCode = evt.getKeyCode();
		//System.out.println(keyCode + " pressed");
		game.keyPressed(keyCode);
	}

}
