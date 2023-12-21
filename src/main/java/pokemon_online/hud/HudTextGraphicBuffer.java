/**
 * 
 */
package pokemon_online.hud;

import java.awt.Color;
import java.awt.Graphics;
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
		
		font = new PkmnFont(HUD_FONT_SPRITE_FILE, 8, 8, 2);  // FIXME Remove hard-coded shit
	}

	public void setVerticalScrollEnabled(boolean b) {
		
	}
	
	/**
	 * Enable/disable text wrapping when reaching the end of a line
	 * @param b
	 */
	public void setWrapTextEnabled(boolean b) {
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
	
	/**
	 * Returns the position of the column cursor in the current row.
	 * When line wrapping is enable, the column cursor goes from <code>0</code> to <code>{@link #getColCount()} - 1</code>
	 * @return The position of the column cursor
	 */
	public int getColCursor() {
		return currCol;
	}
	
	public void writeChar(char c) { // FIXME rename
		
		content.append(c);
		
		if ((currRow >= rowCount)) {
			return; // TODO handle vertical scrolling
		}
		
		if ((currCol >= colCount)) {
			if (wrapLineFlag) {
				nextRow();
			} else {
				return;
			}
		}
		
		int x = currCol*colSpacing;
		int y = currRow*rowSpacing;
		
		grapBuffer.getGraphics().drawImage(font.getGlyph(c), x, y, null);
		
		nextColumn();
	}
	
	public void clear() {
	
		content.setLength(0);
		
		currCol = 0;
		currRow = 0;
		
		Graphics g = grapBuffer.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, grapBuffer.getWidth(), grapBuffer.getHeight()); // TODO handle vertical scrolling
	}
	
	@Override
	public String toString() {
		return "HudTextGraphicBuffer '" + content + "'";
	}

	public Image getImage() {
		return grapBuffer;
	}
}
