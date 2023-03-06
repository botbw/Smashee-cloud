package com.matchmaking.controllers;

import com.matchmaking.services.MMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MMController {
    @Autowired
    private MMService mmService;

    @PostMapping("/mm/add")
    public String addPlayer(@RequestParam MultiValueMap<String, String> json) {
        Integer uid = Integer.parseInt(json.getFirst("uid"));
        Integer rating = Integer.parseInt(json.getFirst("rating"));
        return mmService.addPlayer(uid, rating);
    }

    @PostMapping("/mm/remove")
    public String removePlayer(@RequestParam MultiValueMap<String, String> json) {
        Integer uid = Integer.parseInt(json.getFirst("uid"));
        return mmService.removePlayer(uid);
    }
}
