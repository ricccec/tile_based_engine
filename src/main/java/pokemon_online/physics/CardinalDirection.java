package pokemon_online.physics;

public enum CardinalDirection {

	DIR_UP(-1, Axis.AXIS_Y),
	DIR_DOWN(1, Axis.AXIS_Y),
	DIR_LEFT(-1, Axis.AXIS_X),
	DIR_RIGHT(1, Axis.AXIS_X);
	
	public static CardinalDirection degree2direction(double degrees) {
		
		double normDegrees = degrees % 360;
		normDegrees += (normDegrees < 0) ? 360 : 0;
		
		if (normDegrees <= 45) {
			return CardinalDirection.DIR_RIGHT;
		}
		if ((normDegrees > 45) && (normDegrees <= 135)) {
			return CardinalDirection.DIR_UP;
		}
		if ((normDegrees > 135) && (normDegrees <= 225)) {
			return CardinalDirection.DIR_LEFT;
		}
		if ((normDegrees > 225) && (normDegrees <= 315)) {
			return CardinalDirection.DIR_DOWN;
		}
		// Should never happen
		return null;
	}
	
	final int sign;
	final Axis axis;	
	
	CardinalDirection(int sign, Axis axis) {
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
