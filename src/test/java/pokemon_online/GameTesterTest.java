package pokemon_online;

import org.junit.BeforeClass;
import org.junit.Test;

public class GameTesterTest {

	private static final String START_LAND = "Bosco fosco";
	
	private static final int START_ROW = 5;
	
	private static final int START_COL = 12;
	
	private static GameTester tester;
	
	@BeforeClass
	public static void init() {
		tester = new GameTester();
	}
	@Test
	public void test() {
		tester.setVisible(true);
		tester.startGame(START_LAND, START_ROW, START_COL);
	}

}
