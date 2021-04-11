/**
 * 
 */
package pokemon_online.land;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pokemon_online.Configuration;

/**
 * @author Cecchi
 *
 */
public class LandManager {
	
	private static final Logger LOGGER = Logger.getLogger(LandManager.class);
			
	private static LandManager mgr;
	
	public static LandManager getMgr() {
		if (mgr == null) {
			mgr = new LandManager();
		}
		return mgr;
	}
	
	private final LandBuilder builder;
	
	private final Map<String, Land> lands;
	
	private LandManager() {
		builder = new LandBuilder();
		lands = new HashMap<>();
	}
	
	public Land getLand(String name) throws FileNotFoundException, IOException, ParseException {
		if (!lands.containsKey(name)) {
			loadLand(name);
		}
		return lands.get(name);
	}
	
	public Land loadLand(File lndFile) throws FileNotFoundException, IOException, ParseException {

		//JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        JSONObject landJSON = null;
        try (FileReader reader = new FileReader(lndFile)) {
        	landJSON = (JSONObject)jsonParser.parse(reader);
        }
        
        Land land = builder.buildLand(landJSON);
        
        String lndName = land.getName();
        if (!lands.containsKey(lndName)) {
        	lands.put(lndName, land);
        } else {
        	LOGGER.warn("Land " + lndName + " already loaded");
        }
        
        return lands.get(lndName);
	}
	
	public void loadLand(String name) throws FileNotFoundException, IOException, ParseException {
		File lndFile = new File(Configuration.RESOURCES_DIR, name + ".json");
		loadLand(lndFile);
	}

}
