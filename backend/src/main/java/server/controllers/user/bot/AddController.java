package server.controllers.user.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.service.user.impl.bot.AddService;

import java.util.Map;

@RestController
@RequestMapping("/user/bot")
public class AddController {
    private AddService addService;

    AddController(@Autowired AddService addService) {
        this.addService = addService;
    }

    @PostMapping("/add")
    Map<String, String> add(@RequestParam Map<String, String> json) {
        return addService.add(json);
    }
}
