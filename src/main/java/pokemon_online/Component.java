/**
 * 
 */
package pokemon_online;

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
