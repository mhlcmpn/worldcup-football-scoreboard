package com.football.scoreboard.domain;

import org.apache.commons.lang3.StringUtils;

public class Score {
    private final String team;
    private int score = 0;

    public Score(String team, int score) {
        if (StringUtils.isAllBlank(team)) {
            throw new IllegalArgumentException("Team is mandatory");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score must be a positive number");
        }
        this.team = team;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score must be a positive number");
        }
        this.score = score;
    }

    public String getTeam() {
        return team;
    }
}
