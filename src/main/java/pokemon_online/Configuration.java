/**
 * 
 */
package pokemon_online;

/**
 * @author Cecchi
 *
 */
public class Configuration {

	public static final String RESOURCES_DIR = "input";
	
	public static final long MS_PER_UPDATE = 34; // Around 30 FPS
	
	public static final long FRAMERATE = 25;
	
	/*
	 * Since no object can move slower than 1 pxl/frame, the GAMERATE gives also the speed of the slower object:
	 *  SLOWEST_SPEED = FPS/32 tile/sec
	 */
	public static final int PLAYER_SPEED = 3; // In px/tick
	
	public static final boolean EMPTY_CELLS_WALKABLE = true;  
}
