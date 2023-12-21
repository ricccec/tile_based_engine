/**
 * 
 */
package pokemon_online.game.interaction.event;

/**
 * @author Riccardo
 *
 */
public class EventType {

	public static byte EVENT_QUEUE_END				= 0x0000; // Used by the event manager
	
	public static byte ACTION_PERFORMED				= 0x0001;
	
	public static byte ACTION_B_PERFORMED			= 0x0002;
	
	public static byte HUD_DISPOSED					= 0x0003;
	
	public static byte PUSH_COMPLETED				= 0x0004;
	
	public static byte ZONE_ENTERING				= 0x0005;
	
	public static byte ZONE_EXITING					= 0x0006;
}
