package server.consumers;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.games.greedysnake.Direction;
import server.games.greedysnake.Event;
import server.games.greedysnake.GreedySnake;
import server.mappers.RecordMapper;
import server.mappers.UserMapper;
import server.pojos.User;
import server.utils.JwtUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{jwt}")
public class WebSocketServer { // NOT BEAN
    private static ConcurrentHashMap<Integer, WebSocketServer> userOf = new ConcurrentHashMap<>();

    // the class is not a bean
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    // client session
    private Session session;

    // client user
    private User user;
    // game
    private GreedySnake game;

    public User getUser() {
        return user;
    }


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("jwt") String jwt) throws IOException {
        this.session = session;
        System.out.println("onOpen");
        Integer uid = JwtUtil.parseUserId(jwt);

        this.user = userMapper.selectById(uid);

        if(this.user != null) {
            userOf.put(uid, this);
        } else {
            session.close();
        }
        System.out.println(userOf);
    }

    @OnClose
    public void onClose() {
        System.out.println("onClose");
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        System.out.println("onMsg");
        JSONObject json = JSONObject.parseObject(msg);
        String event = json.getString("event").toUpperCase(Locale.ROOT);
        System.out.println(event);
        if(Event.START.toString().equals(event)) {
            startMatching();
        } else if(Event.CANCEL.toString().equals(event)) {
            cancelMatching();
        } else if(Event.MOVE.toString().equals(event)) {
            move(Direction.getDir(json.getString("direction")));
        } else {
            sendMessage("unknown event");
        }
    }

    private void startMatching() {
        synchronized (pool) {
            pool.add(this.user);

            while(pool.size() >= 2) {
                System.out.println("dispatching");
                Iterator<User> it = pool.iterator();

                User u1 = it.next(), u2 = it.next();
                WebSocketServer socket1 = userOf.get(u1.getUid()), socket2 = userOf.get(u2.getUid());
                pool.remove(u1);
                pool.remove(u2);

                this.game = new GreedySnake(socket1, socket2);
                userOf.get(u1.getUid()).game = this.game;
                userOf.get(u2.getUid()).game = this.game;
                this.game.start();


                JSONObject player1 = new JSONObject();
                player1.put("uid", game.getSnake1().getUid());
                player1.put("x", game.getSnake1().getStX());
                player1.put("y", game.getSnake1().getStY());

                JSONObject player2 = new JSONObject();
                player2.put("uid", game.getSnake2().getUid());
                player2.put("x", game.getSnake2().getStX());
                player2.put("y", game.getSnake2().getStY());

                JSONObject gameInfo = new JSONObject();
                gameInfo.put("player1", player1);
                gameInfo.put("player2", player2);
                gameInfo.put("map", game.getGameMap());

                JSONObject oppo1 = new JSONObject();
                oppo1.put("usrname", u2.getUsrname());
                oppo1.put("avatar", u2.getAvatar());
                JSONObject resp1 = new JSONObject();
                resp1.put("event", Event.START);
                resp1.put("opponent", oppo1);
                resp1.put("gameInfo", gameInfo);
                socket1.sendMessage(resp1.toJSONString());

                JSONObject oppo2 = new JSONObject();
                oppo2.put("uid", u1.getUid());
                oppo2.put("usrname", u1.getUsrname());
                oppo2.put("avatar", u1.getAvatar());
                JSONObject resp2 = new JSONObject();
                resp2.put("event", Event.START);
                resp2.put("opponent", oppo2);
                resp2.put("gameInfo", gameInfo);
                socket2.sendMessage(resp2.toJSONString());

                System.out.println(resp1.toJSONString());
                System.out.println(resp2.toJSONString());
            }
        }
    }

    private void cancelMatching() {

    }

    private void move(Direction dir) {
        if(game.getSnake1().getUid() == user.getUid()) {
            game.setNext1(dir);
        } else if(game.getSnake2().getUid() == user.getUid()) {
            game.setNext2(dir);
        } else {
            throw new RuntimeException();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String msg) {
        try {
            synchronized (this.session) {
                this.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
