package com.matchmaking.services;

import org.springframework.stereotype.Service;

@Service
public class MMServiceImpl implements MMService {
    @Override
    public String addPlayer(Integer uid, Integer rating) {
        System.out.println("add " + uid + " with " + rating);
        return "succeed";
    }

    @Override
    public String removePlayer(Integer uid) {
        System.out.println("remove " + uid);
        return "succeed";
    }
}
