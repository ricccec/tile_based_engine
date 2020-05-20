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
	
	private final Map<String, Image> images;
	
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
	
	public Image getTileImage(String imgName) {
		if (!images.containsKey(imgName)) {
			Image img = loadImage(imgName);
			images.put(imgName, img);
			if (img == null) {
				System.out.println("Error"); // FIXME use a logger
			}
		}
		return images.get(imgName);
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
