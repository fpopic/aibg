package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.BotAction;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;
import hr.best.ai.gl.AbstractPlayer;

public class DangerBot extends AbstractPlayer {
    public DangerBot(String name) {
        super(name);
    }

    int iteracija = 0;

    @Override
    public void sendError(JsonObject jsonObject) {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public JsonElement signalNewState(JsonObject state) throws Exception {

        GameState gameState = new Gson().fromJson(state, GameState.class);

        // My player sizes
        int cnt = gameState.getAgents().get(0).size();

        final int scale = 10;
        final int steps = 10;

        if (iteracija == 1) {
            DangerZone dangerZone = new DangerZone(gameState, scale, steps);
            IOManager.writeText(dangerZone.printValues(), "fajl.txt");
        }
        PlayerAction act = new PlayerAction();
        for (int i = 0; i < cnt; ++i) {
            act.add(new BotAction(true, 0.2, 0.1));
        }
        iteracija++;
        return new Gson().toJsonTree(act);
    }
}
