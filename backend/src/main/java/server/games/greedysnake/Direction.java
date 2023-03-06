package server.games.greedysnake;

import java.util.Locale;

public enum Direction {
    UP, // 0
    RIGHT, // 1
    DOWN,  // 2
    LEFT; // 3
    public static final Integer[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    public static Direction getDir(String dir) {
        if(dir == null) return null;
        if(dir.toUpperCase().equals("UP")) {
            return Direction.UP;
        } else if(dir.toUpperCase().equals("RIGHT")) {
            return Direction.RIGHT;
        } else if(dir.toUpperCase().equals("DOWN")) {
            return Direction.DOWN;
        } else if(dir.toUpperCase().equals("LEFT")) {
            return Direction.LEFT;
        }
        return null;
    }
    public static Integer getId(Direction dir) {
        if(dir == null) return -1;
        if(dir == Direction.UP) return 0;
        else if(dir == Direction.RIGHT) return 1;
        else if(dir == Direction.DOWN) return 2;
        else if(dir == Direction.LEFT) return 3;
        return -1;
    }
}
