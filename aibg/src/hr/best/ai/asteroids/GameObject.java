package hr.best.ai.asteroids;

import java.util.List;

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

	public GameObject newVelocity(Vector vector) {
		return new GameObject(x, y, vector, radius);
	}

	public GameObject move(Vector translationVector) {
		return new GameObject(x + translationVector.getI(), y + translationVector.getJ(), vector, radius);
	}

	public GameObject move() {
		return this.move(vector);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/**
	 * ccw, 0 is right, radians
	 * 
	 * @return
	 */
	public double getAngle() {
		return vector.getAngle();
	}

	public double getSpeed() {
		return vector.getLength();
	}

	public double getRadius() {
		return radius;
	}

	public Vector getVector() {
		return vector;
	}

	public boolean collides(GameObject object) {
		return edgeDistance(this, object) < 0;
	}

	public static double edgeDistance(GameObject o1, GameObject o2) {
		return centerDistance(o1, o2) - o1.radius - o2.radius;
	}

	public static double centerDistance(GameObject o1, GameObject o2) {
		return new Vector(o1, o2).getLength();
	}

	public boolean collides(List<GameObject> objects) {
		return objects.stream().anyMatch(this::collides);
	}

}