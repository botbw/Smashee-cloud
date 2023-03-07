package com.matchmaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MMPool extends Thread {
    private List<Player> pool = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private RestTemplate restTemplate;

    private static final String MM_FOUND_URL = "http://localhost:12345/mm/found";

    public void addPlayer(Integer uid, Integer rating) {
        System.out.println("in add player" + uid);
        lock.lock();
        try {
            pool.removeIf(x -> x.getUid() == uid);
            pool.add(new Player(uid, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer uid) {
        lock.lock();
        try {
            pool.removeIf(x -> x.getUid() == uid);
        } finally {
            lock.unlock();
        }
    }

    private void aging() {
        for(Player player:pool) {
            player.setQueueTime(player.getQueueTime() + 1);
        }
    }

    private boolean isMatched(Player p1, Player p2) {
        Integer ratingDiff = Math.abs(p1.getRating() - p2.getRating());
        Integer minDis = Math.min(p1.getQueueTime(), p2.getQueueTime());
        return ratingDiff <= minDis * 10;
    }

    private void matchFound(Player p1, Player p2) {
        MultiValueMap<String, String> json = new LinkedMultiValueMap<>();
        json.add("uid1", p1.getUid().toString());
        json.add("uid2", p2.getUid().toString());
        System.out.println("found " + p1.toString() + ", " + p2.toString());
        restTemplate.postForObject(MM_FOUND_URL, json, String.class);
    }

    private void findMatched() {
        boolean[] found = new boolean[pool.size()];
        for(int i = 0; i < pool.size(); i++) {
            if(found[i]) continue;
            for(int j = i + 1; j < pool.size(); j++) {
                if(found[j]) continue;
                Player p1 = pool.get(i), p2 = pool.get(j);
                if(isMatched(p1, p2)) {
                    found[i] = found[j] = true;
                    matchFound(p1, p2);
                    break;
                }
            }
        }

        List<Player> newPool = new ArrayList<>();
        for(int i = 0; i < pool.size(); i++) {
            if(!found[i]) newPool.add(pool.get(i));
        }
        pool = newPool;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(pool);
            try {
                Thread.sleep(5000); // 5s
                lock.lock();
                try {
                    aging();
                    findMatched();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
