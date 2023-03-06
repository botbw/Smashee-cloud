package server.service.user.impl.account.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import server.pojos.User;
import server.service.user.impl.account.LoginService;
import server.service.user.impl.UserDetailsImpl;
import server.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private AuthenticationManager authenticationManager;

    public LoginServiceImpl(@Autowired AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Map<String, String> getLoginToken(String usrname, String pwd) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(usrname, pwd);

        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        String jwt = JwtUtil.createJWT(user.getUid().toString());

        Map<String, String> ret = new HashMap<>();
        ret.put("MSG", "succeed");
        ret.put("JWT", jwt);

        return ret;
    }

}
