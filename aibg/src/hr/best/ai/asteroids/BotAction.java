package hr.best.ai.asteroids;

public class BotAction {

	private final boolean shooting;
	private final double rotation;
	private final double acceleration;

    public BotAction(boolean shooting, double rotation, double acceleration) {
        this.shooting = shooting;
		this.rotation = rotation;
		this.acceleration = acceleration;
	}

	public boolean isShooting() {
		return shooting;
	}

	public double getRotation() {
		return rotation;
	}

	public double getAcceleration() {
		return acceleration;
	}

}