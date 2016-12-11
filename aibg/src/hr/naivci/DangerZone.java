package hr.naivci;

import hr.best.ai.asteroids.GameObject;
import hr.best.ai.asteroids.GameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DangerZone {

    protected final int scale;
    protected final int steps;

    protected final int width;
    protected final int height;

    protected GameState gameState;

    protected Map<String, Integer> values;

    public DangerZone(GameState gameState, int scale, int steps) {
        this.gameState = gameState;
        this.scale = scale;
        this.steps = steps;
        this.width = gameState.getParams().getWidth() / scale;
        this.height = gameState.getParams().getHeight() / scale;
        this.values = new HashMap<>();
        calculateDangerZone();
    }

    protected void calculateDangerZone() {
        List<GameObject> asteroids = gameState.getAsteroids();

        for (GameObject asteroid : asteroids) {

            double ax = asteroid.getX();
            double ay = asteroid.getY();

            for (int k = 0; k < 10; k++) {

                double axk = ax + k * asteroid.getVector().getI();
                double ayk = ay + k * asteroid.getVector().getJ();

                int xLeft = (int) Math.max(0.0, Math.floor((axk - asteroid.getRadius()) / scale));
                int xRight = (int) Math.min(width, Math.ceil((axk + asteroid.getRadius()) / scale));
                int yUp = (int) Math.max(0.0, Math.floor((ayk - asteroid.getRadius()) / scale));
                int yDown = (int) Math.min(height, Math.ceil((ayk + asteroid.getRadius()) / scale));

                for (int x = xLeft; x <= xRight; x++) {
                    for (int y = yUp; y < yDown; y++) {
                        int oldValue = values.getOrDefault(x + "," + y, 0);
                        int newValue = 100 - (k * steps);
                        values.put(x + "," + y, oldValue > newValue ? oldValue : newValue);
                    }
                }
            }

//            for (int x = xLeft; x <= xRight; x++) {
//                for (int y = yUp; y < yDown; y++) {
//                    values.put(x + "," + y, 100);
//                }
//            }

        }

//        List<Agent> enemyAgents = gameState.getAgents().get(1);
//
//        for (Agent agent : enemyAgents) {
//            agent.getObject()
//        }

    }

    protected String printValues() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = values.getOrDefault(x + "," + y, 0);
                sb.append(String.format("%3d ", value));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

}
