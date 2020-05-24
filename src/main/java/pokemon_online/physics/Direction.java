package pokemon_online.physics;

public enum Direction {

	DIR_UP(-1, Axis.AXIS_Y),
	DIR_DOWN(1, Axis.AXIS_Y),
	DIR_LEFT(-1, Axis.AXIS_X),
	DIR_RIGHT(1, Axis.AXIS_X);
	
	final int sign;
	final Axis axis;
	
	Direction(int sign, Axis axis) {
		this.sign = sign;
		this.axis = axis;
	}
	
	public boolean isAlongX() {
		return (axis == Axis.AXIS_X);
	}
	
	public boolean isAlongY() {
		return (axis == Axis.AXIS_Y);
	}
	
	public enum Axis {
		AXIS_X,
		AXIS_Y;
	}
	
}
