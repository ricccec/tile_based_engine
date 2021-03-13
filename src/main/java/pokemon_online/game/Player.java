package pokemon_online.game;

import pokemon_online.game.interaction.PkmnControlHandler;
import pokemon_online.game.interaction.event.HudEventHandler;
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
		setPhysicsComponent(new PokemonPhysicsComponent(this));
		grapComp = new SpriteGraphicsComponent(this);
		
		interComp = PkmnControlHandler.getInteractionComponent(this);
		interComp.addEventHandler(new HudEventHandler());
	}
	
}
