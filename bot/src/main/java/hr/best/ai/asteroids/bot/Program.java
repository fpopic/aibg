package hr.best.ai.asteroids.bot;

import com.google.gson.Gson;
import hr.best.ai.asteroids.utility.BotAction;
import hr.best.ai.asteroids.utility.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Program {

    private static BaseBot bot;

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            bot = new ChaserBot("let's do this");

            while (true) {
                GameState state = new Gson().fromJson(reader.readLine(), GameState.class);
                List<BotAction> output = algorithm(state);
                System.out.println(new Gson().toJsonTree(output));
            }
        }
    }

    private static List<BotAction> algorithm(GameState state) {
        return bot.signalNewState(state);
    }
}
