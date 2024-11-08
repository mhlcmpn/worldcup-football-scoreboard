package com.football.scoreboard.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Match {

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;

    private LocalDateTime startTime;

    public Match(String homeTeam, String awayTeam) {
        if (StringUtils.isAllBlank(homeTeam) || StringUtils.isAllBlank(awayTeam)) {
            throw new IllegalArgumentException("Both home team and away team are mandatory");
        }
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void start() {
        homeScore = 0;
        awayScore = 0;
        startTime = LocalDateTime.now();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getAbsolutScore() {
        return homeScore + awayScore;
    }

    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return homeScore == match.homeScore && awayScore == match.awayScore && Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam) && Objects.equals(startTime, match.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, homeScore, awayScore, startTime);
    }
}