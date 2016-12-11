package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.*;
import hr.best.ai.gl.AbstractPlayer;

/**
 * Created by vilimstubican on 11/12/16.
 */
public class ChaserBot extends AbstractPlayer {

    private Target target;
    private Agent me;
    private Agent opponent;

    public ChaserBot(String name) {
        super(name);
    }

    @Override
    public void sendError(JsonObject jsonObject) {

    }

    protected void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public JsonElement signalNewState(JsonObject state) throws Exception {
        GameState st = new Gson().fromJson(state, GameState.class);
        Thread.sleep(60);

        // My player sizes
        int cnt = st.getAgents().get(0).size();
        boolean shooting = calculateShooting();
        double rotation = calculateRotation();
        double acceleration = calculateAcceleration();

        this.me = st.getAgents().get(0).get(0);
        this.opponent = st.getAgents().get(1).get(0);

        setTarget(new Target(opponent.getObject().getX(), opponent.getObject().getY()));

        PlayerAction act = new PlayerAction();
        for (int i = 0; i < cnt; ++i) {
            act.add(new BotAction(shooting, rotation, acceleration));
        }

        JsonElement sol = new Gson().toJsonTree(act);
        return sol;
    }

    protected double calculateAcceleration() {

        if (me != null) {
            if (target != null) {
                return 0.5;
            }
        }

        return 0.0;
    }

    protected boolean isStill(double curX, double curY) {
        double targetX = target.x;
        double targetY = target.y;

        double diffX = curX - targetX;
        double diffY = curY - targetY;

        if (diffX * diffX + diffY + diffY < 200) {
            return true;
        }

        return false;
    }

    protected double calculateRotation() {

        if (me != null) {
            GameObject object = this.me.getObject();
            double curX = object.getX();
            double curY = object.getY();

            if (target != null) {
                // go to target
                double angleRadians = Math.atan2(target.y - curY, target.x - curX);
                return angleRadians - object.getAngle();

            }
        }


        return 0.0;
    }

    protected boolean calculateShooting() {
        if (me != null) {
            GameObject object = this.me.getObject();
            double curX = object.getX();
            double curY = object.getY();

            // aim at the enemy
            double angleRadians = Math.atan2(opponent.getObject().getY() - curY, opponent.getObject().getX() - curX);
            return Math.abs(angleRadians - object.getAngle()) < 0.3;
        }

        return false;
    }


    @Override
    public void close() throws Exception {

    }


    class Target {
        public double x;
        public double y;

        public Target(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
