package server.service.user.impl.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.mappers.BotMapper;
import server.pojos.Bot;
import server.pojos.User;
import server.service.user.UserUtil;
import server.service.user.impl.bot.ModifyService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ModifyServiceImpl implements ModifyService {
    private BotMapper botMapper;

    ModifyServiceImpl(@Autowired BotMapper botMapper) {
        this.botMapper = botMapper;
    }

    @Override
    public Map<String, String> modify(Map<String, String> json) {
        Map<String, String> ret = new HashMap<>();

        Integer id = Integer.parseInt(json.get("id"));
        Bot bot = botMapper.selectById(id);
        if(bot == null) {
            ret.put("MSG", "The bot doesn't exist");
            return ret;
        }
        User user = UserUtil.getUserByAuth();
        if(bot.getUid() != user.getUid()) {
            ret.put("MSG", "Current user doesn't have permission to modify the bot");
            return ret;
        }

        String name = json.get("name");
        if(name == null || name.length() == 0) {
            ret.put("MSG", "Name cannot be empty");
            return ret;
        } else if(name.length() > Bot.MAX_NAME_LENGTH) {
            ret.put("MSG", "Name cannot be more than " + Bot.MAX_NAME_LENGTH + " characters");
            return ret;
        }

        String description = json.get("description");
        if(description != null && description.length() > Bot.MAX_DESCRIPTION_LENGTH) {
            ret.put("MSG", "Description cannot be more than " + Bot.MAX_DESCRIPTION_LENGTH + " characters");
            return ret;
        }

        String code = json.get("code");
        if(code == null || code.length() == 0) {
            ret.put("MSG", "Code cannot be empty");
            return ret;
        } else if(code.length() > Bot.MAX_CODE_LENGTH) {
            ret.put("MSG", "Code cannot be more than " + Bot.MAX_CODE_LENGTH + " characters");
            return ret;
        }

        Date now = new Date();
        Bot newBot = new Bot(bot.getId(), bot.getUid(), name, description, code, bot.getCreatetime(), now);
        botMapper.updateById(newBot);

        ret.put("MSG", "succeed");
        return ret;
    }
}
