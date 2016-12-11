package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.Agent;
import hr.best.ai.asteroids.BotAction;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;
import hr.best.ai.gl.AbstractPlayer;

/**
 * Created by vilimstubican on 11/12/16.
 */
public class MyRandomBot extends AbstractPlayer {
    public MyRandomBot(String name) {
        super(name);
    }

    @Override
    public void sendError(JsonObject jsonObject) {

    }

    @Override
    public JsonElement signalNewState(JsonObject state) throws Exception {
        GameState st = new Gson().fromJson(state, GameState.class);
//        Thread.sleep(60);

        // My player sizes
        int cnt = st.getAgents().get(0).size();
        boolean shooting = true;
        double rotation = 0.1;
        double acceleration = 0.5;

        PlayerAction act = new PlayerAction();
        for (int i = 0; i < cnt; ++i) {
            act.add(new BotAction(shooting, rotation, acceleration));
        }
        JsonElement sol = new Gson().toJsonTree(act);
        return sol;
    }

    @Override
    public void close() throws Exception {

    }
}
