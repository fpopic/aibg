package hr.naivci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hr.best.ai.asteroids.*;
import hr.best.ai.gl.AbstractPlayer;

import java.util.List;

/**
 * Created by vilimstubican on 11/12/16.
 */
public class ChaserBot extends BaseBot {

    private Target target;

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
//        Thread.sleep(60);

        defineAgents(state);

        this.me = myAgents.get(0);

        PlayerAction act = new PlayerAction();

        defineActions(act);

        JsonElement sol = new Gson().toJsonTree(act);
        return sol;
    }

    @Override
    public void defineOpponent(int position) {
        this.opponent = opponentAgents.get(Math.min(opponentAgents.size() - 1, position));
        setTarget(new Target(opponent.getObject().getX(), opponent.getObject().getY()));
    }

    protected double calculateAcceleration() {

        if (me != null) {
            if (target != null) {

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
        double x;
        double y;

        Target(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
