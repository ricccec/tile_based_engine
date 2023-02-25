/**
 * 
 */
package pokemon_online.hud;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pokemon_online.hud.pokemon.GameFont;
import pokemon_online.hud.pokemon.PkmnFont;

/**
 * @author Riccardo
 *
 */
public class HudTextGraphicBuffer {
	
	private static final String HUD_FONT_SPRITE_FILE = "pkmn_GS_charset.png"; // FIXME Remove hard-coded shit
	
	private final BufferedImage grapBuffer;
	
	private final int rowCount;
	
	private final int colCount;
	
	private final int rowSpacing;
	
	private final int colSpacing;
	
	private int currRow;
	
	private int currCol;
	
	private boolean wrapLineFlag;
	
	private final GameFont font;
	
	private final StringBuilder content;
	
	public HudTextGraphicBuffer(int rowCount, int colCount, int rowSpacing, int colSpacing) {
		int width = colCount*colSpacing;
		int height = rowCount*rowSpacing;
		grapBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.rowSpacing = rowSpacing;
		this.colSpacing = colSpacing;
		
		currRow = 0;
		currCol = 0;
		
		content = new StringBuilder();
		
		font = new PkmnFont(HUD_FONT_SPRITE_FILE, 9, 9, 2);  // FIXME Remove hard-coded shit
	}

	public void setScrollEnabled(boolean b) {
		
	}
	
	public void setWrapTextEnabled(boolean b ) {
		wrapLineFlag = b;
	}
	
	public void nextColumn() {
		currCol++;
		if (wrapLineFlag && (currCol >= colCount)) {
			nextRow();
		}
	}
	
	public void nextRow() {
		currRow++;
		currCol = 0;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public int getColCount() {
		return colCount;
	}
	
	public int getRowCursor() {
		return currRow;
	}
	
	public int getColCursor() {
		return currCol;
	}
	
	public void writeChar(char c) { // FIXME rename
		
		content.append(c);
		
		if ((currRow >= rowCount) || (currCol >= colCount)) {
			return; // TODO handle scrolling
		}
		
		int x = currCol*colSpacing;
		int y = currRow*rowSpacing;
		
		grapBuffer.getGraphics().drawImage(font.getGlyph(c), x, y, null);
		
		nextColumn();
	}
	
	@Override
	public String toString() {
		return "HudTextGraphicBuffer '" + content + "'";
	}

	public Image getImage() {
		return grapBuffer;
	}
}
