/**
 * 
 */
package pokemon_online.hud;

import java.awt.Graphics2D;

import pokemon_online.game.Controller;

/**
 * @author Cecchi
 *
 */
public abstract class HudState {

	private HudState parent;
	
	private HudStateStack stateStack;
	
	protected boolean disposed;
	
	public void setParent(HudState parent) {
		this.parent = parent;
	}
	
	public void setStateStack(HudStateStack stack) {
		stateStack = stack;
	}
	
	public abstract void renderHud(int width, int height, Graphics2D grap);
	
	public abstract void update(long dtMillisec, Controller controller);
	
	public void dispose() {
		disposed = true;
	}
	
	public boolean isDisposed() {
		return disposed;
	}
	
	protected HudState getParent() {
		return parent;
	}
	
	protected HudStateStack getStateStack() {
		return stateStack;
	}
	
}
