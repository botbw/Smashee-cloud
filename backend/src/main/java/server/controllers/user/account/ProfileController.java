package server.controllers.user.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.user.impl.account.ProfileService;

import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class ProfileController {
    private ProfileService profileService;

    ProfileController(@Autowired ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping("/getProfile")
    public Map<String, String> getProfile() {
        return profileService.getProfile();
    }

}
