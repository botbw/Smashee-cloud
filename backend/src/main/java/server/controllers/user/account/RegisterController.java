package server.controllers.user.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.service.user.impl.account.RegisterService;

import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class RegisterController {
    private RegisterService registerService;

    public RegisterController(@Autowired RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> json) {
        String usrname = json.get("usrname");
        String pwd = json.get("pwd");
        String confirmedPwd = json.get("confirmedPwd");
        return registerService.register(usrname, pwd, confirmedPwd);
    }
}
