package hr.best.ai.asteroids;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.exceptions.InvalidActionException;
import hr.best.ai.gl.Action;
import hr.best.ai.gl.State;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GameState implements State {

	private final Parameters params;
	private final List<List<Agent>> agents;
	private final List<GameObject> asteroids;
	private final List<GameObject> bullets;
	private final int iteration;

	public GameState(List<List<Agent>> agents, List<GameObject> asteroids, Parameters params, List<GameObject> bullets,
			int iteration) {
		this.agents = agents;
		this.params = params;
		this.asteroids = asteroids;
		this.bullets = bullets;
		this.iteration = iteration;
	}

	public boolean isFinal() {
		return agents.stream().filter(x -> x.stream().anyMatch(Agent::isAlive)).count() <= 1;
	}

	public JsonObject toJSONObject() {
		return toJSONObjectAsPlayer(0);
	}

	public JsonObject toJSONObjectAsPlayer(int playerId) {
		return new Gson().toJsonTree(new GameState(playerIdFirst(playerId), asteroids, params, bullets, iteration))
				.getAsJsonObject();
	}

	// sets first player the one with id playerID
	private List<List<Agent>> playerIdFirst(int playerId) {
		List<List<Agent>> list = getAgents();
		list.add(0, list.remove(playerId));
		return list;
	}

	public State nextState(List<Action> actions) {

		// actions to game actions
		List<PlayerAction> gameActions = actions.stream().map(action -> (PlayerAction) action).collect(toList());

		// asteroids are bounced from edges ( twice for corners )
		List<GameObject> movedAsteroids = asteroids.stream().map(GameObject::move).map(this::bounceFromBounds)
				.map(this::bounceFromBounds).collect(toList());

		// bullets pass through edges
		List<GameObject> movedBullets = bullets.stream().map(GameObject::move).collect(toList());

		// extract asteroid vectors
		List<Vector> asteroidVectors = movedAsteroids.stream().map(GameObject::getVector).collect(toList());

		// extract bullet vectors
		List<Vector> bulletVectors = movedBullets.stream().map(GameObject::getVector).collect(toList());

		// update moved objects vectors for next iteration
		for (int i = 0; i < movedAsteroids.size(); i++) {
			GameObject currentAsteroid = movedAsteroids.get(i);

			for (int j = i + 1; j < movedAsteroids.size(); j++) {
				boolean condition = currentAsteroid.collides(movedAsteroids.get(j));
				if (condition) {
					updateVectors(currentAsteroid, movedAsteroids.get(j), i, j, asteroidVectors, asteroidVectors);
				}
			}
			for (int j = 0; j < movedBullets.size(); j++) {
				boolean condition = currentAsteroid.collides(movedBullets.get(j));
				if (condition) {
					updateVectors(currentAsteroid, movedBullets.get(j), i, j, asteroidVectors, bulletVectors);
				}
			}
		}

		// replace current vectors with newly calculated ones
		List<GameObject> nextAsteroids = bounceObjects(movedAsteroids, asteroidVectors);
		List<GameObject> nextBullets = bounceObjects(movedBullets, bulletVectors);

		List<List<Agent>> nextAgents = moveAgents(agents, gameActions, nextBullets, nextAsteroids);

		return new GameState(nextAgents, nextAsteroids, params, nextBullets, iteration + 1);
	}

	/**
	 * Replaces old vectors in objects with those after object bouncing
	 * calculations
	 * 
	 * @param objects
	 * @param vectors
	 * @return
	 */
	private List<GameObject> bounceObjects(List<GameObject> objects, List<Vector> vectors) {
		List<GameObject> nextObjects = new ArrayList<>();
		for (int i = 0; i < objects.size(); i++) {
			nextObjects.add(objects.get(i).newVelocity(vectors.get(i)));
		}
		return nextObjects;
	}

	private void updateVectors(GameObject object1, GameObject object2, int index1, int index2, List<Vector> vectors1,
			List<Vector> vectors2) {
		Vector effect1 = effectOn(object1, object2);
		Vector effect2 = effectOn(object2, object1);
		Vector sum1 = vectors1.get(index1).add(effect1);
		Vector sum2 = vectors2.get(index2).add(effect2);
		vectors1.remove(index1);
		vectors1.add(index1, sum1);
		vectors2.remove(index2);
		vectors2.add(index2, sum2);
	}

	private Vector effectOn(GameObject hit, GameObject hitter) {
		double ratio = Math.sqrt(hitter.getRadius() / hit.getRadius()) / 3;
		return new Vector(hitter, hit).toUnitVector().times(hitter.getSpeed() * ratio);
	}

	private List<List<Agent>> moveAgents(List<List<Agent>> agents, List<PlayerAction> actions, List<GameObject> bullets,
			List<GameObject> asteroids) {

		List<List<Agent>> nextAgents = new ArrayList<>();

		for (int play_id = 0; play_id < agents.size(); ++play_id) {
			nextAgents.add(new ArrayList<>());
			Iterator<BotAction> actionsIter = actions.get(play_id).iterator();
			for (Agent agent : agents.get(play_id)) {

				if (!agent.isAlive()) { // Only alive bot
					agent.getObject().move();
					continue;
				}

				BotAction action = actionsIter.next();
				GameObject nextObject = moveAgentObject(agent.getObject(), action);

				boolean alive = agent.isAlive() && !outOfBounds(nextObject) && !nextObject.collides(bullets)
						&& !nextObject.collides(asteroids);

				int nextTurnsUntilShooting = agent.getTurnsUntilShooting();
				if (nextTurnsUntilShooting <= 0 && action.isShooting()) {
					bullets.add(shootBullet(nextObject, 0));
					nextTurnsUntilShooting = params.getFireRate();
				} else {
					nextTurnsUntilShooting=Math.max(0, nextTurnsUntilShooting-1);
				}

				nextAgents.get(play_id).add(new Agent(nextObject, nextTurnsUntilShooting, alive, agent.getTeam()));
			}
		}
		return nextAgents;
	}

	private GameObject moveAgentObject(GameObject object, BotAction action) {

		double acceleration = action.getAcceleration();
		double rotation = action.getRotation();

		acceleration = Math.min(acceleration, params.getAccelerationlimit());
		acceleration = Math.max(acceleration, -params.getAccelerationlimit());

		rotation = Math.min(rotation, params.getRotationLimit());
		rotation = Math.max(rotation, -params.getRotationLimit());

		Vector currentVector = object.getVector();
		Vector newVector = currentVector.rotate(rotation)
				.times((currentVector.getLength() + acceleration) / currentVector.getLength());

		if (newVector.getLength() > params.getSpeedLimit())
			newVector = newVector.stretchTo(params.getSpeedLimit());

		// nothing happens if new speed is zero TODO ???
		if (newVector.getLength() < 10E-7)
			newVector = object.getVector();

		return object.newVelocity(newVector).move();
	}

	// TODO update to vector calculations
	private GameObject shootBullet(GameObject agentObject, double angle) {
		double bulletDistance = params.getBulletRadius() + agentObject.getRadius();
		double bulletAngle = agentObject.getAngle() + angle;

		Vector bulletVector = Vector.fromAngleAndSpeed(bulletAngle, params.getBulletSpeed());

		return new GameObject(agentObject.getX() + Math.cos(bulletAngle) * bulletDistance,
				agentObject.getY() + Math.sin(bulletAngle) * bulletDistance, bulletVector, params.getBulletRadius());
	}

	private GameObject bounceFromBounds(GameObject object) {
		double x = object.getX();
		double y = object.getY();
		double i = object.getVector().getI();
		double j = object.getVector().getJ();
		double radius = object.getRadius();

		if (left(object)) {
			x = 2 * radius - x;
			i = -i;
		} else if (right(object)) {
			x = 2 * params.getWidth() - 2 * radius - x;
			i = -i;
		} else if (above(object)) {
			y = 2 * radius - y;
			j = -j;
		} else if (below(object)) {
			y = 2 * params.getHeight() - 2 * radius - y;
			j = -j;
		} else
			return object;
		return new GameObject(x, y, new Vector(i, j), radius);
	}

	private boolean outOfBounds(GameObject object) {
		return left(object) || right(object) || above(object) || below(object);
	}

	private boolean above(GameObject object) {
		return object.getY() - object.getRadius() < 0;
	}

	private boolean below(GameObject object) {
		return object.getY() + object.getRadius() > params.getHeight();
	}

	private boolean right(GameObject object) {
		return object.getX() + object.getRadius() > params.getWidth();
	}

	private boolean left(GameObject object) {
		return object.getX() - object.getRadius() < 0;
	}

	@Override
	public Action parseAction(JsonElement action) throws InvalidActionException {
		return new Gson().fromJson(action, PlayerAction.class);
	}

	@Override
	public int getWinner() {
		// TODO fix this for not final states
		throw new NotImplementedException("");
	}

	// GETTERS

	public List<List<Agent>> getAgents() {
		return defensiveCopy(agents);
	}

	public List<GameObject> getBullets() {
		return defensiveCopy(bullets);
	}

	public List<GameObject> getAsteroids() {
		return defensiveCopy(asteroids);
	}

	public Parameters getParams() {
		return params;
	}

	private <T> List<T> defensiveCopy(List<T> objects) {
		List<T> list = new ArrayList<>();
		list.addAll(objects);
		return list;
	}
}