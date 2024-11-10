package com.football.scoreboard.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * MatchSummary class encapsulates summary of an in-progress match.
 * It will be used for updating the score in {@link com.football.scoreboard.api.service.ScoreboardService}
 * homeTeam -  home team name
 * awayTeam - away team name
 * homeScore - home team score
 * awayScore - away team score
 */
public class MatchSummary implements Serializable {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private final String homeTeam;
    private final String awayTeam;

    private int homeScore;
    private int awayScore;

    public MatchSummary(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore + "\n";
    }
}