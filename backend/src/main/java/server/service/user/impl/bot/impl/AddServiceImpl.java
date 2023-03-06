package server.service.user.impl.bot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.mappers.BotMapper;
import server.mappers.UserMapper;
import server.pojos.Bot;
import server.pojos.User;
import server.service.user.UserUtil;
import server.service.user.impl.bot.AddService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    private BotMapper botMapper;

    AddServiceImpl(@Autowired BotMapper botMapper) {
        this.botMapper = botMapper;
    }


    @Override
    public Map<String, String> add(Map<String, String> json) {
        User user = UserUtil.getUserByAuth();
        Map<String, String> ret = new HashMap<>();

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
        Bot bot = new Bot(null, user.getUid(), name, description, code, null, now, now);
        botMapper.insert(bot);
        ret.put("MSG", "succeed");
        return ret;
    }
}
