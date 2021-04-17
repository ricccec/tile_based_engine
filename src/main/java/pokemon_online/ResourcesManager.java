/**
 * 
 */
package pokemon_online;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pokemon_online.game.rendering.SpriteData;
import pokemon_online.game.utils.GraphicsUtils;
import pokemon_online.land.TileImage;

/**
 * @author Cecchi
 *
 */
public class ResourcesManager {
	
	private static final Logger LOGGER = Logger.getLogger(ResourcesManager.class);
	
	private static ResourcesManager mgr;
	
	public static ResourcesManager getMgr() {
		if (mgr == null) {
			mgr = new ResourcesManager();
		}
		return mgr;
	}
	
	private final Map<String, Map<Float, Image>> images;
	
	private final Map<TileImage, Image> tileImgs;
	
	private final Map<String, SpriteData> graphics;
	
	private final Set<File> directories; // FIXME This dumps all resources in the same "virtual" directory (possible name conflicts)
	
	private ResourcesManager() {
		images = new HashMap<>();
		graphics = new HashMap<>();
		tileImgs = new HashMap<>();
		
		// Get all directories in resource folder
		directories = new HashSet<>();
		File baseDir = new File(Configuration.RESOURCES_DIR);
		for (File dir : FileUtils.listFilesAndDirs(baseDir, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY)) {
			directories.add(dir);
		}
	}
	
	public void addResourceDir(File dir) {
		if (dir.isDirectory()) {
			directories.add(dir);
		} else {
			LOGGER.warn(dir + " is not a directory");
		}
	}
	public SpriteData getGameObjectGraphics(String graphDataName) {
		if (!graphics.containsKey(graphDataName)) {
			SpriteData objGraph = null;
			try {
				objGraph = loadGraphicsData(graphDataName);
			} catch (IOException | ParseException e) {
				e.printStackTrace(); // FIXME Log error
			}
			graphics.put(graphDataName, objGraph);
		}
		return graphics.get(graphDataName);
	}
	
	public Image getTileImage(TileImage tileImg) {
		if (!tileImgs.containsKey(tileImg)) { // Image is not loaded
			// Load tileSet at the given scale factor
			float scaleFactor = tileImg.getScaleFactor();
			Image tileSet = getImage(tileImg.getTileSet().toString(), scaleFactor);
			
			// Crop the tileSet
			int xScaled = Math.round(tileImg.getX()*scaleFactor); // TODO rounding?
			int yScaled = Math.round(tileImg.getY()*scaleFactor);
			int wScaled = Math.round(tileImg.getWidth()*scaleFactor);
			int hScaled = Math.round(tileImg.getHeight()*scaleFactor);
			Image croppedImg = GraphicsUtils.cropImage(tileSet, xScaled, yScaled, wScaled, hScaled);
			tileImgs.put(tileImg, croppedImg);
			
			LOGGER.debug("Tile image " + tileImg + " loaded");
		}
		return tileImgs.get(tileImg);
	}
	
	public Image getImage(String imgName) {
		return getImage(imgName, 1f);
	}
	
	public Image getImage(String imgName, float scaleFactor) {
		
		// Load image at default size
		if (!images.containsKey(imgName)) {
			images.put(imgName, new HashMap<>());
			
			Image img = loadImage(imgName);
			images.get(imgName).put(1f,img);
			if (img == null) {
				LOGGER.warn("Cannot load image " + imgName);
			}
		}
		
		// Resize image
		if (!images.get(imgName).containsKey(scaleFactor)) {
			Image img = images.get(imgName).get(1f);
			if (img == null) {
				images.get(imgName).put(scaleFactor, null);
			} else {
				int imgWidth = img.getWidth(null);
				int imgHeight = img.getHeight(null);
				int newWidth = (int)Math.ceil(imgWidth*scaleFactor);
				int newHeight = (int)Math.ceil(imgHeight*scaleFactor);
				Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
				images.get(imgName).put(scaleFactor, scaledImg);
			}
		}
		return images.get(imgName).get(scaleFactor);
	}
	
	private Image loadImage(String imgName) {
		for (File dir : directories) {
			try {
				Image img = ImageIO.read(new File(dir, imgName));
				return img;
			} catch (IOException e) {}
		}
		return null;
	}
	
	public SpriteData loadGraphicsData(String graphDataName) throws FileNotFoundException, IOException, ParseException {
		// FIXME This requires the json to have the same name of the object, which is not good
		String fileName = graphDataName + ".json";
		File file = findFile(fileName);
		if (file == null) {
			System.out.println("Error"); // FIXME use a logger
			return null;
		}
		
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(file)) {
        	JSONObject graphJSON = (JSONObject)jsonParser.parse(reader);
        	return new SpriteData(graphJSON);
        }
		
	}
	
	private File findFile(String filename) {
		for (File dir : directories) {
			File testFile = new File(dir, filename);
			if (testFile.exists() && (!testFile.isDirectory())) {
				return testFile;
			}
		}
		return null;
	}

}
