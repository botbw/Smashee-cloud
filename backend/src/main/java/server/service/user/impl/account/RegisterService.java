package server.service.user.impl.account;

import java.util.Map;

public interface RegisterService {
    public Map<String, String> register(String usrname, String pwd, String confirmedPwd);
}
