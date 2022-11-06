import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

public class LandJSONizerTest {

	private static final String LAND_DIR = "input/land";
	
	private static final String COMP_DIR = "input/componenti";
	
	private static Land land;
	
	private static Collection<Land> lands;
	
	private static Collection<Componente> comps;
	
	@BeforeClass
	public static void init() throws ClassNotFoundException, IOException {
		
		// Load lands
    	File landsDir = new File(LAND_DIR);
        String[] landsFiles = landsDir.list();

        lands = new ArrayList<>();
        for (String file : landsFiles){
        	if (!FilenameUtils.getExtension(file).equals("lnd"))
        		continue;
        	
        	Land land = (Land)loadObject(new File(LAND_DIR, file));
        	lands.add(land);
         }
	
		// Load components
    	File compsDir = new File(COMP_DIR);
        String[] compsFiles = compsDir.list(); //Creo una lista dei file nella dir componenti

        comps = new ArrayList<>();
        for (String file : compsFiles){
        	if (!FilenameUtils.getExtension(file).equals("txr"))
        		continue;
        	
        	Componente comp = (Componente)loadObject(new File(COMP_DIR, file));
        	comps.add(comp);
         }
	}
	
	@Test
	public void testLands2Json() throws IOException {
		JSONizer jSONizer = new JSONizer();
		for (Land land : lands) {
			JSONObject landJson = jSONizer.jsonizeLand(land);
			
			// Save to file
			jSONizer.writeToFile(landJson, new File(LAND_DIR, land.getNome() + ".json"));
		}
	}
	
	@Test
	public void testComps2Json() throws IOException {
		JSONizer jSONizer = new JSONizer();
		for (Componente comp : comps) {
			JSONObject compJson = jSONizer.jsonizeComponent(comp);
			
			// Save to file
			jSONizer.writeToFile(compJson, new File(COMP_DIR, comp.nome + ".json"));
		}
	}
	
	private static Object loadObject(File objFile) throws ClassNotFoundException, IOException {
		FileInputStream fileIn = new FileInputStream(objFile);
		try(ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			return objectIn.readObject();
		}
	}
	
}
