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
	
	private boolean disposed;
	
	public void setParent(HudState parent) {
		this.parent = parent;
	}
	
	public abstract void renderHud(Graphics2D grap);
	
	public abstract void update(int width, int height, long dtMillisec, Controller controller);
	
	public void dispose() {
		disposed = true;
	}
	
	public boolean isDisposed() {
		return disposed;
	}
	
	public void onDispose(Hud hud) {
		assert(hud.peekState() == this);
		hud.popState();
	}
	
	protected HudState getParent() {
		return parent;
	}
}
