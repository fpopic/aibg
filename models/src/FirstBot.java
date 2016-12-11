import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.BotAction;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;
import hr.best.ai.gl.AbstractPlayer;

import java.util.List;

public class FirstBot extends AbstractPlayer {

    public FirstBot(String name) {
        super(name);
    }

    public void sendError(JsonObject jsonObject) {

    }

    public JsonElement signalNewState(JsonObject state) throws Exception {
        GameState st = (GameState)(new Gson()).fromJson(state, GameState.class);
        Thread.sleep(60L);
        int cnt = ((List)st.getAgents().get(0)).size();
        PlayerAction act = new PlayerAction();

        for(int sol = 0; sol < cnt; ++sol) {
            act.add(new BotAction(true, Math.random() / 2.0D - 0.25D, 0.0D));
        }

        JsonElement var6 = (new Gson()).toJsonTree(act);
        return var6;
    }

    public void close() throws Exception {

    }
}
