package hr.best.ai.asteroids.server;

import com.google.gson.*;
import hr.best.ai.asteroids.Agent;
import hr.best.ai.asteroids.GameObject;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.Parameters;

import hr.best.ai.asteroids.bot.RandomBot;
import hr.best.ai.gl.AbstractPlayer;
import hr.best.ai.gl.GameContext;
import hr.best.ai.gl.State;
import hr.best.ai.server.ProcessIOPlayer;
import hr.best.ai.server.SocketIOPlayer;
import hr.best.ai.server.TimeBucketPlayer;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpp on 12/7/15.
 */
public class ConfigUtilities {
	public static ServerSocket socket = null;

	final static Logger logger = Logger.getLogger(ConfigUtilities.class);


	private static AbstractPlayer instantiatePlayerFromConfig(JsonObject playerConfiguration, int port)
			throws Exception {
		String type = playerConfiguration.get("type").getAsString();
		String name = playerConfiguration.get("name") == null ? "Unknown player"
				: playerConfiguration.get("name").getAsString();

		AbstractPlayer player;
		switch (type) {
		case "random":
			player = new RandomBot(name);
			break;
		case "tcp":
			socket = socket != null ? socket : new ServerSocket(port, 50, null);
			player = new SocketIOPlayer(socket.accept(), name);
			break;
		case "process":
			ArrayList<String> command = new ArrayList<>();
			for (JsonElement e : playerConfiguration.getAsJsonArray("command"))
				command.add(e.getAsString());
			if (playerConfiguration.has("workingDirectory")) {
				player = new ProcessIOPlayer(command,
						Paths.get(playerConfiguration.get("workingDirectory").getAsString()), name);
			} else {
				player = new ProcessIOPlayer(command, name);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown player type. Got: " + type);
		}

		if (playerConfiguration.has("timer")) {
			JsonObject timeBucketConfig = playerConfiguration.get("timer").getAsJsonObject();
			long timePerTurn = timeBucketConfig.get("maxLength").getAsInt();
			player = new TimeBucketPlayer(player, timePerTurn, 5 * timePerTurn);
		}
		return player;
	}

	public static State genInitState(JsonObject config) {
		Parameters params = Parameters.fromJson(config.getAsJsonObject("game"));
		List<List<Agent>> agents = agentsFromJson(config.getAsJsonArray("players"));
		List<GameObject> asteroids = asteroidsFromJson(config.getAsJsonArray("asteroids"));
		List<GameObject> bullets = bulletsFromJson(config.getAsJsonArray("bullets"));
		return new GameState(agents, asteroids, params,bullets,  0);
	}

	private static List<GameObject> asteroidsFromJson(JsonArray asteroidsJson) {
		List<GameObject> asteroids = new ArrayList<>();
		for (JsonElement asteroid : asteroidsJson) {
			asteroid.getAsJsonObject().add("mass", new JsonPrimitive(1));
			asteroids.add(new Gson().fromJson(asteroid, GameObject.class));
		}
		return asteroids;
	}

	private static List<GameObject> bulletsFromJson(JsonArray bulletsJson) {
		List<GameObject> bullets = new ArrayList<>();
		for (JsonElement bullet : bulletsJson) {
			bullets.add(new Gson().fromJson(bullet, GameObject.class));
		}
		return bullets;
	}

	private static List<List<Agent>> agentsFromJson(JsonArray playersJson) {
		List<List<Agent>> sol = new ArrayList<>();
		for (int play_id = 0; play_id < playersJson.size(); ++play_id) {
			JsonElement playerElement = playersJson.get(play_id);
			List<Agent> agents = new ArrayList<>();
			JsonObject player = playerElement.getAsJsonObject();
			JsonArray agentsDesc = player.getAsJsonArray("agent");
			for (JsonElement agentDesc : agentsDesc) {
				agentDesc.getAsJsonObject().add("team", new JsonPrimitive(play_id));
				agents.add(new Gson().fromJson(agentDesc.getAsJsonObject(), Agent.class));
			}
			sol.add(agents);
		}
		return sol;
	}

	public static JsonObject configFromCMDArgs(String[] args) throws FileNotFoundException {
        final JsonParser parser = new JsonParser();

        if (args.length == 0) {
            System.out.println("Falling back to default game configuration.");
            return parser.parse(new InputStreamReader(RunGame.class.getClassLoader().getResourceAsStream("defaultConfig.json"), StandardCharsets.UTF_8)).getAsJsonObject();
        } else {
            System.out.println("Using " + args[0] + " configuration file");
            return parser.parse(new FileReader(args[0])).getAsJsonObject();
        }
    }
	
	public static GameContext configFromString(String message) throws FileNotFoundException {
		JsonObject config = null;
		if (message.equals("undefined")) {
			System.out.println("default");
			config = new JsonParser().parse(new InputStreamReader(
					ConfigUtilities.class.getClassLoader().getResourceAsStream("defaultConfig.json"),
					StandardCharsets.UTF_8)).getAsJsonObject();
		} else {
			System.out.println("imported");
			config = new JsonParser().parse(message).getAsJsonObject();
		}
		List<AbstractPlayer> players = null;
		try {
			players = ConfigUtilities.istantiateAllPlayersFromConfig(config.getAsJsonArray("players"),
					config.get("port").getAsInt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GameState initialState = (GameState) ConfigUtilities.genInitState(config);

		GameContext gc = new GameContext(initialState, players.size());
		players.forEach(gc::addPlayer);
		return gc;
	}

	public static List<AbstractPlayer> istantiateAllPlayersFromConfig(JsonArray playerConfigurations, int port)
			throws Exception {
		List<AbstractPlayer> sol = new ArrayList<>();

		for (JsonElement playerElement : playerConfigurations) {
			JsonObject playerConfiguration = playerElement.getAsJsonObject();
			AbstractPlayer player = instantiatePlayerFromConfig(playerConfiguration, port);
			sol.add(player);
		}
		return sol;
	}
}
