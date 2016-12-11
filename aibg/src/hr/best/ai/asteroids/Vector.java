package hr.best.ai.asteroids;

import com.google.gson.Gson;

public class Vector {

	private final double i;
	private final double j;

	public Vector(double i, double j) {
		if (Double.isNaN(i) || Double.isNaN(j))
			throw new IllegalArgumentException("No NaN allowed");

		this.i = i;
		this.j = j;
	}

	public Vector(GameObject from, GameObject to) {
		this(to.getX() - from.getX(), to.getY() - from.getY());
	}

	/**
	 * @return x component
	 */
	public double getI() {
		return i;
	}

	/**
	 * @return y component
	 */
	public double getJ() {
		return j;
	}

	public double getLength() {
		return Math.sqrt(i * i + j * j);
	}

	public double dot(Vector b) { return i*b.getI() + j*b.getJ();}

	public  double getL2Squared() {return this.dot(this);}

	public double getAngle() {
		return Math.atan2(j, i);
	}

	public Vector add(Vector other) {
		return new Vector(i + other.i, j + other.j);
	}

	public Vector sub(Vector other) {
		return new Vector(i - other.i, j - other.j);
	}

	public Vector times(double scalar) {
		return new Vector(i * scalar, j * scalar);
	}

	public Vector stretchTo(double length) {
		return this.times(length / getLength());
	}

	public Vector toUnitVector() {
		return times(1 / getLength());
	}

	public Vector rotate(double angle) {

		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		return new Vector(i * cos - j * sin, i * sin + j * cos);
	}

	public Vector rotateTo(double angle) {
		return rotate(angle - getAngle());
	}

	public static Vector fromAngleAndSpeed(double angle, double speed) {
		double i = Math.cos(angle) * speed;
		double j = Math.sin(angle) * speed;
		return new Vector(i, j);
	}

	@Override
	public String toString() {
		return new Gson().toJsonTree(this).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Vector vector = (Vector) o;
		return vector.sub(vector).getLength() < 1e-4;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(getI());
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getJ());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
