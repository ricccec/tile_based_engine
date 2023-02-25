/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;

import pokemon_online.Configuration;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;

/**
 * @author Cecchi
 *
 */
public class HudText extends HudState {
	
	private  HudTextEventHandler handler;
	
	private final String[] message;
	
	private final HudTextGraphicBuffer grapBuffer; 
	
	private int textSpeed; // In letters per seconds;
	
	private int currWordIndx;
	
	private int currLettIndx; // index of the right-most letter of the current word that has been shown
	
	private int newCharsToRender; // Num. of chars to write in graph. buffer during the next rendering pass
	
	private int msSinceLastUpdateMs;
	
	public HudText(String s) {
		
		message = s.split("\\P{L}+");
		
		textSpeed = Configuration.DEFAULT_TEXT_SPEED;
		
		currWordIndx = 0;
		currLettIndx = 0;
		
		grapBuffer = new HudTextGraphicBuffer(2, 32, 16, 16); // FIXME VIEW should pass width/height 
		
	}

	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		//grap.setColor(Color.BLUE);
		//grap.drawString(getText(), 32, 32); // TODO
		
		// Write new characters into the graphic buffer
		while ((newCharsToRender > 0) && (updateGraphBuffer())) {
			newCharsToRender--;
		}

		grap.drawImage(grapBuffer.getImage(), 32 , 32, null);

	}

	@Override
	public void update(long dtMillisec, Controller controller) {
		
		// Update counter of chars to display in the next rendering pass
		msSinceLastUpdateMs += dtMillisec;
		int msPerChar = 1000/textSpeed;
		if (msSinceLastUpdateMs >= msPerChar) {
			newCharsToRender += msSinceLastUpdateMs / msPerChar;
			msSinceLastUpdateMs %= msPerChar;
		}
		
		// Player disposes HUD?
		if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1)) {
			if (handler != null) {
				handler.onTextDisplayed(getStateStack());
			}
			dispose();
		}
	}
	
	public void setEventHandler(HudTextEventHandler handler) {
		this.handler = handler;
	}
	
	public interface HudTextEventHandler{
		public void onTextDisplayed(HudStateStack stateStack);
	}
	
	/**
	 * Append a single letter to the graphic buffer
	 * @return
	 */
	private boolean updateGraphBuffer() {
		
		if (currWordIndx >= message.length) {
			// All words have been drawn to the buffer
			return false;
		}
		
		// Current word has no space left? Find next (non-empty) word (list of letter sprites)
		String currWord = message[currWordIndx];
		while (currLettIndx >= currWord.length()) {
			
			// Move word cursor to the next word
			currWordIndx++;
			currLettIndx = 0; // reset letter cursor
			
			// End of text?
			if (currWordIndx >= message.length) {
				return false;
			}
			
			currWord = message[currWordIndx];
		}
		
		// New word? Check if there is room in the current row
		if (currLettIndx == 0) {
			
			int charCount = currWord.length();
			if (grapBuffer.getColCount() >= charCount) { // Words longer than an empty line are just truncated
				int charsLeft = grapBuffer.getColCount() - grapBuffer.getColCursor() - 1;
				// No room: start a new row
				if (charsLeft < charCount + 1) { // +1 because of the initial empty space
					grapBuffer.nextRow();
				}
			}
			
			if (grapBuffer.getColCursor() > 0) {
				// Not a new line: add one empty space 
				grapBuffer.nextColumn();
			}
		}
		
		// Draw letter sprite to the buffer
		char currChar = currWord.charAt(currLettIndx);
		grapBuffer.writeChar(currChar);
		
		currLettIndx++;
		return true;


	}
	
}
