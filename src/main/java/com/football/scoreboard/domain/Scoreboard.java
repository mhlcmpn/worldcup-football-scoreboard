package com.football.scoreboard.domain;

import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {

    private ConcurrentHashMap<String, Match> liveMatches = new ConcurrentHashMap<>();

    public Match findMatch(String key) {
        return liveMatches.get(key);
    }

    public Match updateMatch(String key, Match match) {
        return liveMatches.put(key, match);
    }

    public void addMatch(String key, Match match) {
        liveMatches.put(key, match);
    }

    public void removeMatch(String key) {
        liveMatches.remove(key);
    }

    public ConcurrentHashMap<String, Match> getLiveMatches() {
        return liveMatches;
    }
}
