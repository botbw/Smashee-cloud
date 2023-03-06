package server.controllers.user.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.service.user.impl.account.LoginService;

import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class LoginController {
    private LoginService loginService;

    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/getToken")
    public Map<String, String> getToken(@RequestParam Map<String, String> json) {
        String usrname = json.get("usrname");
        String pwd = json.get("pwd");
        return loginService.getLoginToken(usrname, pwd);
    }

}
