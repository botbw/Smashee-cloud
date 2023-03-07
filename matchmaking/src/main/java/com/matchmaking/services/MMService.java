package com.matchmaking.services;

import org.springframework.stereotype.Service;

public interface MMService {
    public String addPlayer(Integer uid, Integer rating);
    public String removePlayer(Integer uid);
}
