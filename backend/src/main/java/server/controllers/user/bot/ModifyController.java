package server.controllers.user.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.service.user.impl.bot.ModifyService;

import java.util.Map;

@RestController
@RequestMapping("/user/bot")
public class ModifyController {
    private ModifyService modifyService;

    ModifyController(@Autowired ModifyService modifyService) {
        this.modifyService = modifyService;
    }

    @PostMapping("/modify")
    public Map<String, String> modify(@RequestParam Map<String, String> json) {
        return modifyService.modify(json);
    }

}
