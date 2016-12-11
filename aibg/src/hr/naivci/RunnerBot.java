package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.Agent;
import hr.best.ai.asteroids.GameObject;
import hr.best.ai.asteroids.GameState;
import hr.best.ai.asteroids.PlayerAction;

public class RunnerBot extends BaseBot {

    private Target target;
    private Agent me;
    private Agent opponent;

    public RunnerBot(String name) {
        super(name);
        this.setTarget(new Target(1700.0, 900.0));
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
//        Thread.sleep(30);

        defineAgents(st);

        this.opponent = opponentAgents.get(0);
        if(!opponent.isAlive()) {
            this.opponent = opponentAgents.get(1);
        }

        PlayerAction act = new PlayerAction();

        defineActions(act);

        JsonElement sol = new Gson().toJsonTree(act);
        return sol;
    }

    protected double calculateAcceleration() {

        if (me != null) {
            GameObject object = this.me.getObject();
            double curX = object.getX();
            double curY = object.getY();

            if (target != null) {

                if (isStill(curX, curY)) {
                    return -0.5;
                }

                if(me.getObject().getSpeed() > 2) {
                    return -0.5;
                }

                if(me.getObject().getSpeed() < 1 ) {
                    return 0.1;
                }
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
                if (!isStill(curX, curY)) {
                    // go to target
                    double angleRadians = Math.atan2(target.y - curY, target.x - curX);
                    return angleRadians - object.getAngle();
                }
            }

            // aim at the enemy
            double angleRadians = Math.atan2(opponent.getObject().getY() - curY, opponent.getObject().getX() - curX);
            return angleRadians - object.getAngle();
        }


        return 0.0;
    }

    protected boolean calculateShooting() {
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
