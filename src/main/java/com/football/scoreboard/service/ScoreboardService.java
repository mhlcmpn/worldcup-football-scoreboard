package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;

public interface ScoreboardService {
    void startMatch(String homeTeam, String awayTeam);

    Match findMatch(String homeTeam, String awayTeam);

    void finishMatch(String homeTeam, String awayTeam);
}
