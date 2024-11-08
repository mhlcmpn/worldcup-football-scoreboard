package com.football.scoreboard.api.model;

import com.football.scoreboard.api.exception.InvalidScoreValueException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * The Score class encapsulates team name and score value.
 * It will be used for updating the score in {@link com.football.scoreboard.api.service.ScoreboardService}
 *  team - team name
 *  score - absolute score
 */
public class Score {
    private final String team;
    private int score = 0;

    public Score(String team, int score) {
        if (StringUtils.isAllBlank(team)) {
            throw new IllegalArgumentException("Team is mandatory");
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

    public String getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return score == score1.score
                && StringUtils.compareIgnoreCase(team, score1.team) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(StringUtils.upperCase(team), score);
    }
}