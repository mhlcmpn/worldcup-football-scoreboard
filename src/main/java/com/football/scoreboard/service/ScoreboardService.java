package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;
import com.football.scoreboard.domain.Score;

import java.util.Collection;

public interface ScoreboardService {
    void startMatch(String homeTeam, String awayTeam);

    Match findMatch(String homeTeam, String awayTeam);

    void finishMatch(String homeTeam, String awayTeam);

    void updateScore(Score homeScore, Score awayScore);

    Collection<Match> buildLiveMatchesSummary();
}
