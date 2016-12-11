package hr.best.ai.asteroids.bot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.BotAction;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;
import hr.best.ai.gl.AbstractPlayer;
import org.apache.log4j.Logger;

public class RandomBot extends AbstractPlayer {
	final static Logger logger = Logger.getLogger(RandomBot.class);

	public RandomBot(String name) {
		super(name);
	}

	@Override
	public void close() throws Exception {

	}

	@Override
	public void sendError(JsonObject message) {

	}

	@Override
	public JsonElement signalNewState(JsonObject state) throws Exception {
		GameState st = new Gson().fromJson(state, GameState.class);
		Thread.sleep(60);
		int cnt = st.getAgents().get(0).size();
		PlayerAction act = new PlayerAction();
		for (int i = 0; i < cnt; ++i) {
			act.add(new BotAction(true, Math.random() / 2 - 0.25, 0));
		}
		JsonElement sol = new Gson().toJsonTree(act);
		return sol;

	}

}