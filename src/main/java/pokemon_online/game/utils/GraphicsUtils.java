/**
 * 
 */
package pokemon_online.game.utils;

import java.awt.Graphics2D;

/**
 * @author Cecchi
 *
 */
public class GraphicsUtils {

	public static Graphics2D translate(Graphics2D g2d, int dx, int dy) {
		Graphics2D result = (Graphics2D)g2d.create();
		result.translate(dx, dy);
		//System.out.println(g2d.getClipBounds());
		return result;
	}
}
