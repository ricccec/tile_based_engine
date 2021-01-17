/**
 * 
 */
package pokemon_online.game.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
	
	public static BufferedImage cropImage(Image img, int x, int y, int w, int h) {
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(img, -x, -y, null);
		g.dispose();
		return newImage;
	}
}
