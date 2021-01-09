/**
 * 
 */
package pokemon_online.game;

/**
 * @author Cecchi
 *
 */
public abstract class Component {

	protected final GameObject obj;
	
	public Component(GameObject obj) {
		this.obj = obj;
	}
}
