package hr.best.ai.asteroids;

import com.google.gson.JsonObject;

public class Parameters {

	private final int width;
	private final int height;
	private final double rotationLimit;
	private final double speedLimit;
	private final double accelerationLimit;
	private final double bulletSpeed;
	private final double bulletRadius;
	private final int fireRate;


	public Parameters(int width, int height, double rotationLimit, double speedLimit, double accelerationLimit,
			double bulletSpeed, double bulletRadius, int fireRate) {
		this.width = width;
		this.height = height;
		this.rotationLimit = rotationLimit;
		this.speedLimit = speedLimit;
		this.accelerationLimit = accelerationLimit;
		this.bulletSpeed = bulletSpeed;
		this.bulletRadius = bulletRadius;
		this.fireRate = fireRate;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getBulletSpeed() {
		return bulletSpeed;
	}

	public double getRotationLimit() {
		return rotationLimit;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public double getAccelerationlimit() {
		return accelerationLimit;
	}

	public double getBulletRadius() {
		return bulletRadius;
	}

	public int getFireRate() {
		return fireRate;
	}

	public static Parameters fromJson(JsonObject object) {
		return new Parameters(object.get("width").getAsInt(), object.get("height").getAsInt(),
				object.get("rotationLimit").getAsDouble(), object.get("speedLimit").getAsDouble(),
				object.get("accelerationLimit").getAsDouble(), object.get("bulletSpeed").getAsDouble(),
				object.get("bulletRadius").getAsDouble(), object.get("fireRate").getAsInt());
	}
}
