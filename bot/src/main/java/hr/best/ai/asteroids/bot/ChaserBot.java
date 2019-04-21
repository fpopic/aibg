package hr.best.ai.asteroids.bot;

import hr.best.ai.asteroids.naivci.SearchableDangerZone;
import hr.best.ai.asteroids.utility.GameObject;
import hr.best.ai.asteroids.utility.GameState;
import hr.best.ai.asteroids.utility.PlayerAction;

public class ChaserBot extends BaseBot {

    private Target target;

    protected SearchableDangerZone dangerZone;

    public ChaserBot(String name) {
        super(name);
    }


    protected void setTarget(Target target) {
        this.target = target;
    }

    public PlayerAction signalNewState(GameState st) {
        int scale = 10;
        int steps = 20;
        int safetyLimit = 10;
        dangerZone = new SearchableDangerZone(st, scale, steps, safetyLimit);

        defineAgents(st);

        PlayerAction act = new PlayerAction();

        defineActions(act);

        return act;
    }

    @Override
    public void defineOpponent(int position) {
        this.opponent = opponentAgents.get(Math.min(opponentAgents.size() - 1, position));

        if (dangerZone != null && (dangerZone.isInDangerous(me.getObject().getX(), me.getObject().getY()))) {
            SearchableDangerZone.Position pos = dangerZone.getClosestSafe(
                    me.getObject().getX(), me.getObject().getY(), me.getObject().getVector().getI(), me.getObject().getVector().getJ());
            setTarget(new Target(pos.x, pos.y));
        } else {
            setTarget(new Target(opponent.getObject().getX(), opponent.getObject().getY()));
        }
    }

    protected double calculateAcceleration() {

        if (me != null) {
            if (target != null) {
                if (dangerZone != null
                        && dangerZone.isInDangerous(
                        me.getObject().getX(),
                        me.getObject().getY())) {
                    if (me.getObject().getSpeed() < 5) {
                        return 0.5;
                    } else {
                        return -0.1;
                    }
                }

                if (me.getObject().getSpeed() > 3) {
                    return -0.5;
                }

                if (me.getObject().getSpeed() < 1) {
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
//        return false;
        if (me != null) {
            GameObject object = this.me.getObject();
            double curX = object.getX();
            double curY = object.getY();

            // aim at the enemy
            double angleRadians = Math.atan2(opponent.getObject().getY() - curY, opponent.getObject().getX() - curX);
            return Math.abs(angleRadians - object.getAngle()) < 0.2;
        }

        return false;
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
