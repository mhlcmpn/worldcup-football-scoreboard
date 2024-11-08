package com.football.scoreboard.impl.model;

import com.football.scoreboard.api.model.Match;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internal scoreboard thread safe representation
 */
public final class Scoreboard {

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

    public Collection<Match> getLiveMatches() {
        return liveMatches.values();
    }
}
