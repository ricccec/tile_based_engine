package pokemon_online.game;

import pokemon_online.game.rendering.SpriteGraphicsComponent;
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
		grapComp = new SpriteGraphicsComponent(this);
	}

}
