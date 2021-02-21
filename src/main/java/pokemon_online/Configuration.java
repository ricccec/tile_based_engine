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
	
	
	/**
	 * Game world integration delta
	 */
	public static final long MS_PER_UPDATE = 50; // Around 20 FPS
	
	public static final long FRAMERATE = 20; // Frames per second
	
	/*
	 * Since no object can move slower than 1 pxl/frame, the gamerate gives also the speed of the slower object:
	 *  SLOWEST_SPEED = 32/MS_PER_UPDATE tile/sec
	 */
	public static final int PLAYER_SPEED = 4; // In px/tick
	
	public static final boolean EMPTY_CELLS_WALKABLE = true;  
	
	public static final int CELL_SIZE_PXLS = 32;
	
	public static final float TILE_ANIMATION_SPEED = 2; // Frames per second
}
