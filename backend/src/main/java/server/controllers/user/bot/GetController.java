package server.controllers.user.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.pojos.Bot;
import server.service.user.impl.bot.GetService;

import java.util.List;

@RestController
@RequestMapping("/user/bot")
public class GetController {
    @Autowired
    private GetService getService;

    @GetMapping("/get")
    public List<Bot> get() {
        return getService.get();
    }
}
