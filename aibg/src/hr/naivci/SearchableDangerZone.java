package hr.naivci;

import hr.best.ai.asteroids.GameState;

public class SearchableDangerZone extends DangerZone {

    protected int limit;
    public SearchableDangerZone(GameState gameState, int scale, int steps, int limit) {
        super(gameState, scale, steps);
        this.limit = limit;
    }

    public boolean isInDangerous(double xPos, double yPos) {
        int curX = (int) Math.floor(xPos / scale);
        int curY = (int) Math.floor(yPos / scale);

        String key = curX + "," + curY;
        return values.containsKey(key) && values.get(key) > limit;
    }

    public Position getClosestSafe(double xPos, double yPos) {

        int curX = (int) Math.floor(xPos / scale);
        int curY = (int) Math.floor(yPos / scale);

        int step = 1;

        while(step < 7) {
            String tmpKeyLeft = "," + (curY - step);
            String tmpKeyRight = "," + (curY + step);
            for(int x = curX - step; x < curX + step; x++) {
                String key = x + "," + tmpKeyLeft;
                if(values.containsKey(key) && values.get(key) <= limit) {
                    return new Position((x + 0.5) * scale, (curY - step + 0.5) * scale);
                }
                key = x + "," + tmpKeyRight;
                if(values.containsKey(key) && values.get(key) <= limit) {
                    return new Position((x + 0.5) * scale, (curY + step + 0.5) * scale);
                }
            }
            tmpKeyLeft = (curX - step) + "," ;
            tmpKeyRight = (curY + step) + ",";
            for(int y = curY - step; y < curY + step; y++) {
                String key = tmpKeyLeft + y;
                if(values.containsKey(key) && values.get(key) <= limit) {
                    return new Position((curX - step + 0.5) * scale, (y + 0.5) * scale);
                }
                key = tmpKeyRight + y;
                if(values.containsKey(key) && values.get(key) <= limit) {
                    return new Position((curX + step + 0.5) * scale, (y + 0.5) * scale);
                }
            }

            step++;
        }

        return new Position(curX, curY);
    }

    public class Position {
        public double x;
        public double y;

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}
