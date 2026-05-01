package pokemon_online.game;

import pokemon_online.game.interaction.PkmnGameActionHandler;
import pokemon_online.game.interaction.interactions.HudInteractionHandler;
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
		
		interComp = PkmnGameActionHandler.getInteractionComponent(this);
		interComp.addInteractionHandler(new HudInteractionHandler());
	}

	public PokemonPhysicsComponent getPhysicsComponent() {
		return (PokemonPhysicsComponent)super.getPhysicsComponent();
	}
	
}
