/**
 * 
 */
package pokemon_online.game.ia;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Cecchi
 *
 */
public class Path {

	private final Collection<Segment> segments;
	
	public Path() {
		segments = new ArrayList<>();
	}
	
	public void addSegment(Segment segment) {
		segments.add(segment);
	}
	
	public Collection<Segment> getSegmentsCrossing(int objRow, int objCol) {
		return segments;
	}

}
