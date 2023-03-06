package server.games.greedysnake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    private static final Integer INIT_LEN = 3;
    private final Integer uid;
    private final Integer stX;
    private final Integer stY;

    public final List<Direction> dirHis;

    public Snake(Integer uid, Integer stX, Integer stY) {
        this.uid = uid;
        this.stX = stX;
        this.stY = stY;
        this.dirHis = new ArrayList<>();
    }

    public Integer getStX() {
        return stX;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getStY() {
        return stY;
    }

    private boolean checkLongerSnake(Integer step) {
        if(this.dirHis.size() <= this.INIT_LEN) return true;
        else if(step != Snake.INIT_LEN && (step - this.INIT_LEN) % 5 == 0) return true;
        return false;
    }

    public List<SnakeCell> getCells() {
        List<SnakeCell> cells = new LinkedList<>();

        cells.add(new SnakeCell(this.stX, this.stY));
        Integer x = this.stX, y = this.stY;

        System.out.println(dirHis);

        for(int step = 0; step < dirHis.size(); step++) {
            Direction dir = dirHis.get(step);
            x += Direction.dx[Direction.getId(dir)];
            y += Direction.dy[Direction.getId(dir)];

            cells.add(new SnakeCell(x, y));

            if(!checkLongerSnake(step)) {
                cells.remove(0);
            }
        }
        return cells;
    }

    public String getDirHisString() {
        StringBuilder sb = new StringBuilder();

        for(Direction dir:dirHis) {
            Integer id = Direction.getId(dir);
            sb.append(id);
        }

        return sb.toString();
    }
}
