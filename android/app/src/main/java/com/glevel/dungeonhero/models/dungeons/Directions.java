package com.glevel.dungeonhero.models.dungeons;

/**
 * Created by guillaume ON 10/8/14.
 */
public enum Directions {
    NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

    private int dx, dy;

    private Directions(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Directions from(float deltaX, float deltaY) {
        if (deltaX < 0) {
            return WEST;
        } else if (deltaX > 0) {
            return EAST;
        } else if (deltaY > 0) {
            return NORTH;
        } else {
            return SOUTH;
        }
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
    
}
