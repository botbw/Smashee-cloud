package server.controllers.matchmaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.service.matchmaking.MMStartGame;

@RestController
public class MMController {
    @Autowired
    private MMStartGame mmStartGame;

    @PostMapping("/mm/found")
    public String matchFound(@RequestParam MultiValueMap<String, String> json) {
        Integer uid1 = Integer.parseInt(json.getFirst("uid1"));
        Integer uid2 = Integer.parseInt(json.getFirst("uid2"));
        return mmStartGame.matchFound(uid1, uid2);
    }
}
