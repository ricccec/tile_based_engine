/**
 * 
 */
package pokemon_online.land.zone;

import java.awt.Color;
import java.awt.Graphics2D;

import pokemon_online.Configuration;
import pokemon_online.game.GameObject;
import pokemon_online.game.GameWorld;
import pokemon_online.game.interaction.InteractionComponent;
import pokemon_online.game.interaction.event.Event;
import pokemon_online.game.interaction.event.EventHandler;
import pokemon_online.game.rendering.GraphicsComponent;
import pokemon_online.game.rendering.Viewport;
import pokemon_online.game.utils.GameUtils;

/**
 * @author Cecchi
 *
 */
public abstract class Zone extends GameObject {

	public Zone() {
		setInteractionComponent(new InteractionComponent(this));
		getInteractionComponent().addEventHandler(new EventHandler() {
			@Override
			public boolean handleEvent(GameWorld world, GameObject receiver, Event evt) {
				switch(evt.getType()) {
					case ZONE_EXITING:
						onExiting(world, receiver, evt.getArgument(0));
						return true;
					case ZONE_ENTERING:
						onEntering(world, receiver, evt.getArgument(0));
						return true;
					default:
						return false;
				}
			}
		});
		
		setGraphicsComponent(new GraphicsComponent(this) {
			
			@Override
			public void updateAnimation(long dt) {}
			
			@Override
			public void render(Graphics2D grap, Viewport viewport) {// FIXME Move to an utility class
				if (Configuration.DEBUG) {
					int zoneScrX = viewport.getScreenX() + obj.getX();
					int zoneScrY = viewport.getScreenY() + obj.getY();
					
					grap.setColor(getColor());
					grap.fillRect(zoneScrX, zoneScrY, Configuration.CELL_SIZE_PXLS, Configuration.CELL_SIZE_PXLS);
				}
			}
		});
	}
	
	protected abstract void onExiting(GameWorld world, GameObject zone, GameObject entity);

	protected abstract void onEntering(GameWorld world, GameObject zone, GameObject entity);
	
	protected abstract Color getColor();
}
