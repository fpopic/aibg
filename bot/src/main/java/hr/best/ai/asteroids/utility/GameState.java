package hr.best.ai.asteroids.utility;

import java.util.List;

public class GameState {

    private final int iteration;

    private final Parameters params;

    private final List<List<Agent>> agents;

    private final List<GameObject> asteroids;

    private final List<GameObject> bullets;

    public GameState(int iteration, Parameters params, List<List<Agent>> agents, List<GameObject> asteroids,
                     List<GameObject> bullets) {
        this.iteration = iteration;
        this.params = params;
        this.agents = agents;
        this.asteroids = asteroids;
        this.bullets = bullets;
    }

    public int getIteration() {
        return iteration;
    }

    public Parameters getParams() {
        return params;
    }

    public List<List<Agent>> getAgents() {
        return agents;
    }

    public List<GameObject> getBullets() {
        return bullets;
    }

    public List<GameObject> getAsteroids() {
        return asteroids;
    }

}