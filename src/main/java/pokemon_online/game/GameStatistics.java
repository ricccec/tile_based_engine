/**
 * 
 */
package pokemon_online.game;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Cecchi
 *
 */
public class GameStatistics {
	
	private static final float AVG_INERTIA = 1f;
	
	private long tickCount;
	
	private float avgMsPerUpdate;
	
	private long beforeUpdtTime;

	public void beforeUpdate() {
		beforeUpdtTime = System.currentTimeMillis();
	}
	
	public void afterUpdate() {
		
		tickCount++;
		
		// Compute avg. update time
		long elapsed = System.currentTimeMillis() - beforeUpdtTime;
		if (tickCount > 1) {
			avgMsPerUpdate = (AVG_INERTIA*(elapsed) + (1-AVG_INERTIA)*avgMsPerUpdate);
		} else {
			avgMsPerUpdate = elapsed;
		}
	}
	
	public void print(Graphics2D grap) {
		// FIXME Remove hard-coded coords
		grap.setColor(Color.BLUE);
		grap.drawString("Ticks: " + tickCount, 0, 0);
		grap.drawString("Avg. ms per update: " + avgMsPerUpdate, 0, 16);
	}
}
