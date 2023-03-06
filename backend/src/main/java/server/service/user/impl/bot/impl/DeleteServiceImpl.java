package server.service.user.impl.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.mappers.BotMapper;
import server.pojos.Bot;
import server.pojos.User;
import server.service.user.UserUtil;
import server.service.user.impl.bot.DeleteService;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteServiceImpl implements DeleteService {
    private BotMapper botMapper;

    DeleteServiceImpl(@Autowired BotMapper botMapper) {
        this.botMapper = botMapper;
    }

    @Override
    public Map<String, String> delete(Map<String, String> json) {
        Integer id = Integer.parseInt(json.get("id"));
        Bot bot = botMapper.selectById(id);

        Map<String, String> ret = new HashMap<>();

        if(bot == null) {
            ret.put("MSG", "The bot doesn't exist");
            return ret;
        }

        User loginUser = UserUtil.getUserByAuth();

        if(bot.getUid() != loginUser.getUid()) {
            ret.put("MSG", "Current user doesn't have permission to delete this bot");
            return ret;
        }

        botMapper.deleteById(bot.getId());
        ret.put("MSG", "succeed");
        return ret;
    }
}
