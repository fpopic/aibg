package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.Agent;
import hr.best.ai.asteroids.BotAction;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;
import hr.best.ai.gl.AbstractPlayer;

import java.util.List;

/**
 * Created by vilimstubican on 11/12/16.
 */
public abstract class BaseBot extends AbstractPlayer {

    protected List<Agent> myAgents;
    protected List<Agent> opponentAgents;
    protected Agent me;
    protected Agent opponent;

    protected int myTeamNumber = 0;

    public BaseBot(String name) {
        super(name);
    }

    public void defineAgents(GameState st) {

        Agent meCandidate = st.getAgents().get(0).get(0);
        if(meCandidate.getTeam() == myTeamNumber) {
            myAgents = st.getAgents().get(0);
            opponentAgents = st.getAgents().get(1);
        } else {
            myAgents = st.getAgents().get(1);
            opponentAgents = st.getAgents().get(0);
        }
    }

    public void defineActions(PlayerAction act) {
        // My players sizes
        int cnt = myAgents.size();

        for (int i = 0; i < cnt; ++i) {
            defineOpponent(i);
            defineMe(i);

            boolean shooting = calculateShooting();
            double rotation = calculateRotation();
            double acceleration = calculateAcceleration();
            act.add(new BotAction(shooting, rotation, acceleration));
        }
    }

    public void defineMe(int position) {
        this.me = myAgents.get(position);
    }

    public void defineOpponent(int position) {
        this.opponent = opponentAgents.get(0);
    }

    abstract boolean calculateShooting();
    abstract double calculateRotation();
    abstract double calculateAcceleration();
}
