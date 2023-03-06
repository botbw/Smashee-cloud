package server.games.greedysnake;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import server.consumers.WebSocketServer;
import server.mappers.RecordMapper;
import server.pojos.Record;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GreedySnake extends Thread{ // generate symmetric map
    private static final Integer ROW_CNT = 13;
    private static final Integer COL_CNT = 14;
    private static final Integer WALL_CNT = 40;

    private boolean[][] gameMap;
    private final WebSocketServer socket1, socket2;

    private final ReentrantLock lock;
    private Direction next1, next2;

    private final Snake snake1, snake2;

    public Status getStatus() {
        return status;
    }

    private Status status;

    public GreedySnake(WebSocketServer socket1, WebSocketServer socket2) {
        int time = 0;
        final int timeout = 1000;
        while (time < timeout) {
            this.gameMap = draw();
            if (dfs(this.gameMap,1, 1, ROW_CNT - 2, COL_CNT - 2)) {
                break;
            }
            time++;
        }
        this.socket1 = socket1;
        this.socket2 = socket2;
        Integer uid1 = socket1.getUser().getUid(), uid2 = socket2.getUser().getUid();
        this.snake1 = new Snake(uid1, 1, 1);
        this.snake2 = new Snake(uid2, ROW_CNT - 2, COL_CNT - 2);
        this.lock = new ReentrantLock();
        this.next1 = this.next2 = null;
        this.status = Status.IN_GAME;
    }


    public void setNext1(Direction next1) {
        lock.lock();
        try {
            this.next1 = next1;
        } finally {
            lock.unlock();
        }
    }

    public void setNext2(Direction next2) {
        lock.lock();
        try {
            this.next2 = next2;
        } finally {
            lock.unlock();
        }
    }

    boolean[][] draw() {
        boolean[][] map = new boolean[ROW_CNT][COL_CNT];

        for (int i = 0; i < ROW_CNT; i++) {
            map[i][0] = map[i][COL_CNT - 1] = true;
        }

        for (int j = 0; j < COL_CNT; j++) {
            map[0][j] = map[ROW_CNT - 1][j] = true;
        }
        int cnt = 0;
        int time = 0;
        final int timeout = 1000;

        Random r = new Random();

        while (cnt < WALL_CNT && time < timeout) {
            time++;
            int i = r.nextInt(ROW_CNT);
            int j = r.nextInt(COL_CNT);

            if ((i == 1 && j == 1) || (i == ROW_CNT - 2 && j == COL_CNT - 2)) continue;
            if (map[i][j]) continue;
            map[i][j] = map[ROW_CNT - i - 1][COL_CNT - j - 1] = true;
            cnt += 2;
        }
        return map;
    }

    boolean dfs(boolean[][] map, int x, int y, int dstX, int dstY) {
        if (x == dstX && y == dstY) return true;
        map[x][y] = true;

        for (int i = 0; i < 4; i++) {
            int xx = x + Direction.dx[i], yy = y + Direction.dy[i];
            if (xx >= 0 && yy >= 0 && xx < COL_CNT && yy < COL_CNT && !map[xx][yy])
                if (dfs(map, xx, yy, dstX, dstY)) {
                    map[x][y] = false;
                    return true;
                }
        }
        map[x][y] = false;
        return false;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++) { // long loop
            System.out.println(i);
            if(checkNextStep()) { // got next step input for both
                this.status = judge();
                System.out.println(this.status.toString());
                if(this.status == Status.IN_GAME) {
                    broadcastMove();
                } else { // game ends
                    saveResult();
                    broadcastResult();
                    break;
                }
            } else { // game ends
                lock.lock();
                try {
                    if(next1 == null && next2 == null) {
                        status = Status.DRAW;
                    } else if(next1 == null) {
                        status = Status.SNAKE2_WIN;
                    } else if(next2 == null) {
                        status = Status.SNAKE1_WIN;
                    }
                } finally {
                    lock.unlock();
                }
                System.out.println("??");

                broadcastResult();
                break;
            }
        }
    }

    private boolean checkNextStep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100); // 100 * 100 sleep
                lock.lock();
                try {
                    if(next1 != null && next2 != null) {
                        snake1.dirHis.add(next1);
                        snake2.dirHis.add(next2);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void broadcastMsg(String msg) {
        socket1.sendMessage(msg);
        socket2.sendMessage(msg);
    }

    private void saveResult() {
        Record r = new Record(
                null,
                snake1.getUid(),
                snake2.getUid(),
                snake1.getStX(),
                snake1.getStY(),
                snake2.getStX(),
                snake2.getStY(),
                snake1.getDirHisString(),
                snake2.getDirHisString(),
                ROW_CNT,
                COL_CNT,
                getGameMapString(),
                status.toString(),
                new Date()
        );

        WebSocketServer.recordMapper.insert(r);
    }

    private void broadcastResult() {
        JSONObject resp = new JSONObject();

        resp.put("event", Event.END);
        resp.put("status", status);

        broadcastMsg(resp.toJSONString());
    }

    private void broadcastMove() {
        JSONObject resp = new JSONObject();

        resp.put("event", Event.MOVE);

        lock.lock();
        try {
            resp.put("player1", next1);
            resp.put("player2", next2);
            next1 = next2 = null;
        } finally {
            lock.unlock();
        }
        broadcastMsg(resp.toJSONString());
    }

    private boolean isSnakeValid(List<SnakeCell> me, List<SnakeCell> other) {
        // do not hit the wall
        Integer sizeMe = me.size();
        SnakeCell head = me.get(sizeMe - 1);
        if(gameMap[head.x][head.y]) return false;
        // do not hit myself
        for(SnakeCell cell:me) {
            if(cell == head) break;
            if(cell.x == head.x && cell.y == head.y) return false;
        }
        // do not hit others
        for(SnakeCell cell:other) {
            if(cell.x == head.x && cell.y == head.y) return false;
        }
        return true;
    }

    private Status judge() {
        List<SnakeCell> cells1 = snake1.getCells(), cells2 = snake2.getCells();

        boolean snake1Valid = isSnakeValid(cells1, cells2);
        boolean snake2Valid = isSnakeValid(cells2, cells1);

        if(snake1Valid && snake2Valid) {
            return Status.IN_GAME;
        } else if(snake1Valid && !snake2Valid) {
            return Status.SNAKE1_WIN;
        } else if(!snake1Valid && snake2Valid) {
            return Status.SNAKE2_WIN;
        } else {
            return Status.DRAW;
        }
    }


    public boolean[][] getGameMap() {
        return gameMap;
    }

    public String getGameMapString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ROW_CNT; i++) {
            for(int j = 0; j < COL_CNT; j++) {
                int tmp = gameMap[i][j] ? 1 : 0;
                sb.append(tmp);
            }
        }
        return sb.toString();
    }
    public Snake getSnake1() {
        return snake1;
    }

    public Snake getSnake2() {
        return snake2;
    }


}
