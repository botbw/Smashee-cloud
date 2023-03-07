package com.matchmaking.services;

import com.matchmaking.MMPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MMServiceImpl implements MMService {
    public static MMPool mmPool;

    @Autowired
    public void setMmPool(MMPool mmPool) {
        MMServiceImpl.mmPool = mmPool;
    }

    @Override
    public String addPlayer(Integer uid, Integer rating) {
        mmPool.addPlayer(uid, rating);
        return "succeed";
    }

    @Override
    public String removePlayer(Integer uid) {
        System.out.println("remove " + uid);
        mmPool.removePlayer(uid);
        return "succeed";
    }
}
