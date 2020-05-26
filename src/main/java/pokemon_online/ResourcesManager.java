/**
 * 
 */
package pokemon_online;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * @author Cecchi
 *
 */
public class ResourcesManager {
	
	private static ResourcesManager mgr;
	
	public static ResourcesManager getMgr() {
		if (mgr == null) {
			mgr = new ResourcesManager();
		}
		return mgr;
	}
	
	private final Map<String, Map<Float, Image>> images;
	
	private final Collection<File> directories; // FIXME This dumps all resources in the same "virtual" directory
	
	private ResourcesManager() {
		images = new HashMap<>();
		
		// Get all directories in resource folder
		directories = new ArrayList<>();
		File baseDir = new File(Configuration.RESOURCES_DIR);
		for (File dir : FileUtils.listFilesAndDirs(baseDir, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY)) {
			directories.add(dir);
		}
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
				System.out.println("Error"); // FIXME use a logger
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
	
	public Image loadImage(String imgName) {
		for (File dir : directories) {
			try {
				Image img = ImageIO.read(new File(dir, imgName));
				return img;
			} catch (IOException e) {}
		}
		return null;
	}

}
