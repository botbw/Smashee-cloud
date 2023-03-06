package server.service.user.impl.account.impl;

import org.springframework.stereotype.Service;
import server.pojos.User;
import server.service.user.impl.account.ProfileService;

import java.util.HashMap;
import java.util.Map;

import static server.service.user.UserUtil.getUserByAuth;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Override
    public Map<String, String> getProfile() {
        User user = getUserByAuth();

        Map<String, String > ret = new HashMap<>();
        ret.put("MSG", "succeed");
        ret.put("id", user.getUid().toString());
        ret.put("usrname", user.getUsrname());
        ret.put("avatar", user.getAvatar());

        return ret;
    }


}
