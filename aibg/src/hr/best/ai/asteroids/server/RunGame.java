package hr.best.ai.asteroids.server;

import com.google.gson.JsonObject;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.gl.AbstractPlayer;
import hr.best.ai.gl.GameContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class RunGame {

	final static Logger logger = Logger.getLogger(RunGame.class);
	private static WebSocketObserver ws;
	public static void main(String[] args) {
		try {
			JsonObject config = ConfigUtilities.configFromCMDArgs(args);

			final List<AbstractPlayer> players = ConfigUtilities
					.istantiateAllPlayersFromConfig(config.getAsJsonArray("players"), config.get("port").getAsInt());

			GameState initialState = (GameState) ConfigUtilities.genInitState(config);

			try (GameContext gc = new GameContext(initialState, 2, 7)) {
				players.forEach(gc::addPlayer);

				if (config.get("visualization").getAsBoolean()) {
					ws = new WebSocketObserver(config.get("ws_port").getAsInt());
					ws.start();
					gc.addObserver(ws);
					logger.info("Awaiting connection onto websocket");
					while (ws.connections().size() == 0) {
						Thread.sleep(100);
					}
					logger.info("Someone connected, can start game");
				}
				gc.play();

				if (ws != null) {
					ws.close();
					ws.stop();
				}
			}

		} catch (Exception ex) {
			logger.error(ex);
			ex.printStackTrace();
		}
		finally {
			if (ConfigUtilities.socket != null) {
				try {
					ConfigUtilities.socket.close();
				} catch (IOException ignorable) {
				}
			}

			if (ws != null) {
				try {
					ws.close();
				} catch (Exception ignorable) {}
				try {
					ws.stop();
				} catch (Exception ignorable) {}
			}
		}
	}

}
