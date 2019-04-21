package hr.best.ai.asteroids.bot;


import hr.best.ai.asteroids.utility.Agent;
import hr.best.ai.asteroids.utility.BotAction;
import hr.best.ai.asteroids.utility.GameState;
import hr.best.ai.asteroids.utility.PlayerAction;

import java.util.List;

public abstract class BaseBot {

    protected List<Agent> myAgents;
    protected List<Agent> opponentAgents;
    protected Agent me;
    protected Agent opponent;
    protected String name;

    protected int myTeamNumber = 0;

    public BaseBot(String name) {
        this.name = name;
    }

    public void defineAgents(GameState st) {
            myAgents = st.getAgents().get(0);
            opponentAgents = st.getAgents().get(1);
    }

    public void defineActions(PlayerAction act) {
        // My players sizes
        int cnt = myAgents.size();

        for (int i = 0; i < cnt; ++i) {
            defineMe(i);
            defineOpponent(i);

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

    abstract PlayerAction signalNewState(GameState st);
}
