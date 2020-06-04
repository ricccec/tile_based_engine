package pokemon_online;

import org.junit.BeforeClass;
import org.junit.Test;

import pokemon_online.game.GraphicsComponent;
import pokemon_online.game.ia.IAComponent;
import pokemon_online.game.ia.Path;
import pokemon_online.game.ia.RandomIAComponent;
import pokemon_online.game.ia.Segment;
import pokemon_online.game.rendering.ObjectGraphicsData;
import pokemon_online.physics.PokemonPhysicsComponent;

public class GameTesterTest {

	private static final String START_LAND = "Pokecity";
	
	private static final int START_ROW = 10;
	
	private static final int START_COL = 12;
	
	private static GameTester tester;
	
	@BeforeClass
	public static void init() {
		tester = new GameTester();
	}
	
	@Test
	public void test() {
		
		GameObject obj = new GameObject();
		IAComponent iaComponent = new RandomIAComponent(obj);
		obj.setIAComponent(iaComponent);
		GraphicsComponent gComp = new GraphicsComponent(obj);
		ObjectGraphicsData gData = ResourcesManager.getMgr().getGameObjectGraphics("F Allenatrice");
		gData.setGraphics(gComp);
		obj.setGraphicsComponent(gComp);
		PokemonPhysicsComponent phComp = new PokemonPhysicsComponent(obj);
		obj.setPhysicsComponent(phComp);
		
		
		tester.setPlayerSprite("F Allenatrice");
		tester.setVisible(true);
		tester.startGame(START_LAND, START_ROW, START_COL);
		
		tester.spawnObject(obj, 12, 8);
	}

}
