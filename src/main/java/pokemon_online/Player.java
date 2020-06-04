package pokemon_online;

import pokemon_online.game.Controller;
import pokemon_online.game.GraphicsComponent;
import pokemon_online.physics.PokemonPhysicsComponent;
/**
 * 
 */

/**
 * @author Cecchi
 *
 */
public class Player extends GameObject {
	
	public Player() {
		physComp = new PokemonPhysicsComponent(this);
		grapComp = new GraphicsComponent(this);
	}

}
