package com.football.scoreboard.domain;

import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {

    private ConcurrentHashMap<String, Match> liveMatches = new ConcurrentHashMap<>();

    public Match findMatch(String key) {
        return liveMatches.get(key);
    }

    public Match addMatch(String key, Match match) {
        return liveMatches.put(key, match);
    }

    public ConcurrentHashMap<String, Match> getLiveMatches() {
        return liveMatches;
    }
}
