package server.controllers.user.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.service.user.impl.bot.DeleteService;

import java.util.Map;

@RestController
@RequestMapping("/user/bot")
public class DeleteController {
    private DeleteService deleteService;

    DeleteController(@Autowired DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    @PostMapping("/delete")
    Map<String, String> delete(@RequestParam Map<String, String> json) {
        return deleteService.delete(json);
    }
}