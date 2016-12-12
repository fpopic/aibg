package hr.naivci;

import hr.best.ai.asteroids.GameState;

public class SearchableDangerZone extends DangerZone {

    protected int limit;
    protected int margin;


    public SearchableDangerZone(GameState gameState, int scale, int steps, int limit) {
        super(gameState, scale, steps);
        this.limit = limit;
        this.margin = 150 / scale;
    }

    public boolean isInDangerous(double xPos, double yPos) {
        int curX = (int) Math.floor(xPos / scale);
        int curY = (int) Math.floor(yPos / scale);


        String key = curX + "," + curY;

        return values.containsKey(key) && values.get(key) > limit;
    }

    public Position getClosestSafe(double xPos, double yPos, double i, double j) {

        int curX = (int) Math.floor(xPos / scale);
        int curY = (int) Math.floor(yPos / scale);

        int step = 6;
        int type = (i > 0 ? 0 : 2) + (j > 0 ? 0 : 1);


        Position best = null;
        int best_value = Integer.MAX_VALUE;
        while (step < 30) {
            int x1, x2, x3, x4, y1, y2, y3, y4;
            switch (type) {
                case 0: // bottom right
                    x1 = curX + step;
                    y1 = curY + step;
                    x2 = curX + step;
                    y2 = curY - step;
                    x3 = curX - step;
                    y3 = curY + step;
                    x4 = curX - step;
                    y4 = curY - step;
                    break;
                case 1: // top right
                    x1 = curX + step;
                    y1 = curY - step;
                    x2 = curX + step;
                    y2 = curY + step;
                    x3 = curX - step;
                    y3 = curY - step;
                    x4 = curX - step;
                    y4 = curY + step;
                    break;
                case 2: // bottom left
                    x1 = curX - step;
                    y1 = curY + step;
                    x2 = curX - step;
                    y2 = curY - step;
                    x3 = curX + step;
                    y3 = curY + step;
                    x4 = curX + step;
                    y4 = curY - step;
                    break;
                // top left
                default:
                    x1 = curX - step;
                    y1 = curY - step;
                    x2 = curX - step;
                    y2 = curY + step;
                    x3 = curX + step;
                    y3 = curY - step;
                    x4 = curX + step;
                    y4 = curY + step;
                    break;
            }

            String key1 = x1 + "," + y1;
            String key2 = x2 + "," + y2;
            String key3 = x3 + "," + y3;
            String key4 = x4 + "," + y4;

            if (x1 > margin && x1 < width - margin && y1 > margin && y1 < height - margin &&
                    values.containsKey(key1) && values.get(key1) <= limit) {

                int softpenalty = 0;
                if (values.get(key1) + softpenalty < best_value) {
                    best = new Position((x1 + 0.5) * scale, (y1 + 0.5) * scale);
                    best_value = values.get(key1);
                }
            }

            if (x2 > margin && x2 < width - margin && y2 > margin && y2 < height - margin &&
                    values.containsKey(key2) && values.get(key2) <= limit) {

                int softpenalty = 0;

                if (values.get(key2) + softpenalty < best_value) {
                    best = new Position((x2 + 0.5) * scale, (y2 + 0.5) * scale);
                    best_value = values.get(key2);
                }

            }

            if (x3 > margin && x3 < width - margin && y3 > margin && y3 < height - margin &&
                    values.containsKey(key3) && values.get(key3) <= limit) {

                int softpenalty = 0;

                if (values.get(key3) + softpenalty < best_value) {
                    best = new Position((x3 + 0.5) * scale, (y3 + 0.5) * scale);
                    best_value = values.get(key3);
                }
            }

            if (x4 > margin && x4 < width - margin && y4 > margin && y4 < height - margin &&
                    values.containsKey(key4) && values.get(key4) <= limit) {

                int softpenalty = 0;

                if (values.get(key4) + softpenalty < best_value) {
                    best = new Position((x4 + 0.5) * scale, (y4 + 0.5) * scale);
                    best_value = values.get(key4);
                }
            }

            step++;
        }

        if (best != null) {
            return best;
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