package hr.best.ai.asteroids.utility;

public class Agent {

	private final GameObject object;
	private final int turnsUntilShooting;
	private final boolean alive;
	private final int team;


	public Agent(GameObject object, int turnsUntilShooting, boolean alive, int team) {
		this.object = object;
		this.turnsUntilShooting = turnsUntilShooting;
		this.alive = alive;
		this.team = team;
	}

	public GameObject getObject() {
		return object;
	}

	public int getTurnsUntilShooting() {
		return turnsUntilShooting;
	}

	public boolean isAlive() {
		return alive;
	}

	public int getTeam() {
		return this.team;
	}
}