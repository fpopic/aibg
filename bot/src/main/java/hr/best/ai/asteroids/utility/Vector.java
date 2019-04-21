package hr.best.ai.asteroids.utility;

public class Vector {

	private final double i;
	private final double j;

	public Vector(double i, double j) {
		this.i = i;
		this.j = j;
	}

	public double getI() {
		return i;
	}

	public double getJ() {
		return j;
	}

	public double getLength() {
		return Math.sqrt(i * i + j * j);
	}

	public double getAngle() {
		return Math.atan2(j, i);
	}
}
