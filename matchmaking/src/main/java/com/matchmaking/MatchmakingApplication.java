package com.matchmaking;

import com.matchmaking.services.MMServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchmakingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchmakingApplication.class, args);
        MMServiceImpl.mmPool.run();
    }

}
