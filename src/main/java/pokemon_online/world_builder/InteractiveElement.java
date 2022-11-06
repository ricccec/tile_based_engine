/**
 * 
 */
package pokemon_online.world_builder;

/**
 * @author Cecchi
 *
 */
public interface InteractiveElement {
	
	public void mousePress(java.awt.event.MouseEvent evt);
	
	public void mouseRelease(java.awt.event.MouseEvent evt);
	
	public void mouseMove(java.awt.event.MouseEvent evt);
	
	public void mouseExit(java.awt.event.MouseEvent evt);
	
	public void focusLost();
}
