package server.service.matchmaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.consumers.WebSocketServer;
import server.mappers.UserMapper;
import server.pojos.User;

@Service
public class MMStartGameImpl implements MMStartGame {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String matchFound(Integer uid1, Integer uid2) {
        System.out.println("match found " + uid1 + ", " + uid2);
        User u1 = userMapper.selectById(uid1), u2 = userMapper.selectById(uid2);
        WebSocketServer.matchFound(u1, u2);
        return "succeed";
    }
}
