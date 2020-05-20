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
		ctrl = new Controller();
		physComp = new PokemonPhysicsComponent(this);
		grapComp = new GraphicsComponent(this);
	}


	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
