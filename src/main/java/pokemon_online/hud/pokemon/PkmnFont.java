/**
 * 
 */
package pokemon_online.hud.pokemon;

import java.awt.Image;
import java.io.File;

import pokemon_online.ResourcesManager;
import pokemon_online.land.CroppedImage;

/**
 * @author Riccardo
 *
 */
public class PkmnFont implements GameFont {

	private final Image fontSprite;
	
	private final File fontSpriteFile;
	
	private final int charWidth;
	
	private final int charHeight;
	
	private final float scaleFactor;
	
	private final int spriteGridRowsCount;
	
	private final int spriteGridColsCount;
	
	public PkmnFont(String imgName, int charWidth, int charHeight, float scaleFactor) {
		
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.scaleFactor = scaleFactor;
		
		fontSpriteFile = new File(imgName);
		
		fontSprite = ResourcesManager.getMgr().getImage(imgName);
		spriteGridRowsCount = Math.floorDiv(fontSprite.getHeight(null), charHeight);
		spriteGridColsCount = Math.floorDiv(fontSprite.getWidth(null), charWidth);
	}
	
	@Override
	public Image getGlyph(char c) {
		
		int charRow = (int) Math.ceil((c + 1f)/spriteGridColsCount) - 1;
		int charCol = ((int)c) % spriteGridColsCount;
		
		int charX = charCol*charWidth;
		int charY = charRow*charHeight;
		
		// System.out.println("Character " + c + "(" + (int)c + ") is at r:" + charRow + " c:" + charCol + "(" + charX + ", " + charY + ")");
		
		CroppedImage charSprite = new CroppedImage(fontSpriteFile, charX, charY, charWidth, charHeight, scaleFactor);
		
		return ResourcesManager.getMgr().getCroppedImage(charSprite);
	}
}
