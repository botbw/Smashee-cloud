package server.service.user.impl.account;

import java.util.Map;

public interface LoginService {
    public Map<String, String> getLoginToken(String usrname, String pwd);
}
