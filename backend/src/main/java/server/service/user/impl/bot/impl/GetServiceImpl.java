package server.service.user.impl.bot.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.mappers.BotMapper;
import server.pojos.Bot;
import server.pojos.User;
import server.service.user.UserUtil;
import server.service.user.impl.bot.GetService;

import java.util.List;

@Service
public class GetServiceImpl implements GetService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public List<Bot> get() {
        User user = UserUtil.getUserByAuth();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", user.getUid());

        return botMapper.selectList(queryWrapper);
    }
}
