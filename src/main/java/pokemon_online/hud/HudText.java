/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;
import java.awt.Image;

import org.apache.log4j.Logger;

import pokemon_online.Configuration;
import pokemon_online.ResourcesManager;
import pokemon_online.game.Controller;
import pokemon_online.game.Controller.Control;

/**
 * @author Cecchi
 *
 */
public class HudText extends HudState {
	
	private static final Logger LOGGER = Logger.getLogger(HudText.class);
			
	private enum HudTestState {
		FILLING_BUFFER,	// Filling the characters buffer
		BUFFER_FULL, 	// Buffer is full. More stuff to write
		MSG_SHOWN;
	}
	
	private  HudTextEventHandler handler;
	
	private final String[] textWords;
	
	private final HudTextGraphicBuffer grapBuffer; 
	
	private HudTestState status;
	
	private int textSpeed; // In letters per seconds;
	
	private int currWordIndx;
	
	private int currLettIndx; // Cursor to the right-most letter of the last word added to the buffer that has been shown
	
	private int newCharsToRender; // Num. of chars to write in graph. buffer during the next rendering pass
	
	private int msSinceLastUpdateMs;
	
	public HudText(String s) {
		
		textWords = s.split("\\P{L}+");
		
		textSpeed = Configuration.DEFAULT_TEXT_SPEED;
		
		status = HudTestState.FILLING_BUFFER;
		currWordIndx = 0;
		currLettIndx = 0;
		
		grapBuffer = new HudTextGraphicBuffer(2, 8, 16, 16); // FIXME VIEW should pass width/height
		grapBuffer.setWrapTextEnabled(true);
		
	}

	@Override
	public void renderHud(int width, int height, Graphics2D grap) {
		//grap.setColor(Color.BLUE);
		//grap.drawString(getText(), 32, 32); // TODO
		
		if (status == HudTestState.FILLING_BUFFER) {
			// Write new characters into the graphic buffer
			while ((newCharsToRender > 0) && (updateGraphBuffer())) {
				newCharsToRender--;
			}
			newCharsToRender = 0;
		}

		grap.drawImage(grapBuffer.getImage(), 32 , 32, null);
		
		if (status == HudTestState.BUFFER_FULL) {
			// Draw a blinking arrow
			boolean isSecEven = ((msSinceLastUpdateMs/500)%2)==0;
			if (isSecEven) {
				Image imgArrow = ResourcesManager.getMgr().getImage("small_arrow_dwn.png", 2);
				grap.drawImage(imgArrow, 32, 32, null);
			}
		}

	}

	@Override
	public void update(long dtMillisec, Controller controller) {
		
		switch (status) {
			case FILLING_BUFFER:
				if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1)) {
					// Player is in a hurry: show the entire message
					newCharsToRender = Integer.MAX_VALUE;
				} else {
					// Update counter of chars to display in the next rendering pass
					msSinceLastUpdateMs += dtMillisec;
					int msPerChar = 1000/textSpeed;
					if (msSinceLastUpdateMs >= msPerChar) {
						newCharsToRender += msSinceLastUpdateMs / msPerChar;
						msSinceLastUpdateMs %= msPerChar;
					}
				}
				break;
			case BUFFER_FULL:
				if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1)) {
					// Player wants to read the rest. Clear buffer and resume message writing
					grapBuffer.clear();
					status = HudTestState.FILLING_BUFFER;
					msSinceLastUpdateMs = 0;
				} else {
					msSinceLastUpdateMs += dtMillisec;
				}
				break;
			case MSG_SHOWN:
				
				if (controller.isStatusChanged(Control.ACTION_1) && controller.isActive(Control.ACTION_1)) {
					// Player disposes HUD?
					if (handler != null) {
						handler.onTextDisplayed(getStateStack());
					}
					dispose();
				}
				break;
		default:
			break;
		
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
		
		if (currWordIndx >= textWords.length) {
			// All words have been drawn to the buffer
			status = HudTestState.MSG_SHOWN;
			return false;
		}
		
		if (status == HudTestState.BUFFER_FULL) {
			return false;
		}
		
		// Current word has no more letters to add to the buffer? Find next (non-empty) word
		String currWord = textWords[currWordIndx];
		while (currLettIndx >= currWord.length()) {
			
			// Move word cursor to the next word
			currWordIndx++;
			currLettIndx = 0; // reset letter cursor
			
			// End of text?
			if (currWordIndx >= textWords.length) {
				status = HudTestState.MSG_SHOWN;
				return false;
			}
			
			currWord = textWords[currWordIndx];
		}
		
		// Set position of graphic buffer's cursor
		boolean buffFull = findRoomForWord(currWord);
		if (!buffFull) {
			status = HudTestState.BUFFER_FULL;
			msSinceLastUpdateMs = 0;
			LOGGER.debug("HUD's graph. buffer is full");
			return false;
		}
		
		// Draw letter sprite to the buffer
		char currChar = currWord.charAt(currLettIndx);
		grapBuffer.writeChar(currChar);
		status = HudTestState.FILLING_BUFFER;
		LOGGER.debug(currChar + " pushed into the text buffer");
		
		currLettIndx++;
		return true;


	}
	
	
	/**
	 * Find room in the graph. buffer for the given word and place the buffer's cursor accordingly.
	 * @param word
	 * @return <code>true</code> if there is room in the buffer. <code>false</code> otherwise.
	 */
	private boolean findRoomForWord(String word) {
		
		
		
		// New word? Add an empty space after the previous word (except when we are at the beginning of a new line)
		if ((currLettIndx == 0) && (grapBuffer.getColCursor() > 0)) {
			LOGGER.debug("SPACE pusged into the text buffer");
			grapBuffer.nextColumn(); // NB: Since WRAPPING is enabled, this my cause the cursor to jump to the next row
		}
		
		// Words that are LONGER than a line are just splitted across multiple lines
		if (word.length() >= grapBuffer.getColCount()) {
			
			// No  space in the current line? Check if there's room for a new line
			if (grapBuffer.getRowCursor() >= grapBuffer.getRowCount()) {
				return false;
			}
			return true;
		}
		
		// Words that are SHORTER than a line are always written without splitting
		
		if (currLettIndx == 0) {
			// New word? Check if there is room in the current line
			int lineFreeCols = grapBuffer.getColCount() - grapBuffer.getColCursor();
			if (lineFreeCols < word.length()) {
				// No room: start a new line
				grapBuffer.nextRow();
				if (grapBuffer.getRowCursor() >= grapBuffer.getRowCount()) {
					// Buffer might not have space for a new line
					
					return false;
				}
				return true;
			}
		} else {
			assert(grapBuffer.getColCursor() < grapBuffer.getColCount());
		}
		return true;
	}
	
}
