package hr.best.ai.asteroids.utility;

public class GameObject {

	private final double x;
	private final double y;
	private final Vector vector;
	private final double radius;

	public GameObject(double x, double y, Vector vector, double radius) {
		this.x = x;
		this.y = y;
		this.vector = vector;
		this.radius = radius;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}

	public Vector getVector() {
		return vector;
	}

	public double getAngle() {
		return vector.getAngle();
	}

	public double getSpeed() {
		return vector.getLength();
	}

}