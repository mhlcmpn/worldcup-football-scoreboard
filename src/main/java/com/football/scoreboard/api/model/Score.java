package com.football.scoreboard.api.model;

import com.football.scoreboard.api.exception.InvalidScoreValueException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The Score class encapsulates team name and score value.
 * It is used as input for updateScore method in {@link com.football.scoreboard.api.service.ScoreboardService}
 * team - team name
 * score - absolute score
 */
public class Score implements Serializable {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private final String team;
    private int score = 0;

    public Score(String team, int score) {
        if (StringUtils.isAllBlank(team)) {
            throw new IllegalArgumentException("Team name is mandatory");
        }
        if (score < 0) {
            throw new InvalidScoreValueException("Score must be a positive number");
        }
        this.team = team;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTeam() {
        return team;
    }
}
