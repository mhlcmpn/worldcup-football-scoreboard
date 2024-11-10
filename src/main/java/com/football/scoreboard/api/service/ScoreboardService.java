package com.football.scoreboard.api.service;

import com.football.scoreboard.api.model.MatchSummary;
import com.football.scoreboard.api.model.Score;

import java.util.List;

/**
 * ScoreboardService interface defines the public APi for handling in-progress matches scoreboard
 */
public interface ScoreboardService {

    /**
     * Start a match and add it to the scoreboard. Initial score is 0-0.
     * The method will throw
     * {@link com.football.scoreboard.api.exception.MatchAlreadyStartedException} if the match has already been started
     * {@link java.lang.IllegalArgumentException} if the team names are missing or not valid
     * Constraints: homeTeam and awayTeam are mandatory and must have different values
     * @param homeTeam home team name (mandatory)
     * @param awayTeam away team name (mandatory)
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Mark a match as finished and remove it from scoreboard.
     * The method will throw {@link com.football.scoreboard.api.exception.NotFoundMatchException} if the match is not found on the scoreboard
     * @param homeTeam home team name (mandatory)
     * @param awayTeam away team name (mandatory)
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Update scores for match on the scoreboard.
     * The method will throw
     * {@link com.football.scoreboard.api.exception.InvalidScoreValueException} if the score value is not a positive number
     * {@link com.football.scoreboard.api.exception.NotFoundMatchException} if the match is not on the scoreboard (not in-progress)
     * {@link java.lang.IllegalArgumentException} if the team names are missing
     *
     * @param homeScore {@link com.football.scoreboard.api.model.Score} (mandatory)
     * @param awayScore {@link com.football.scoreboard.api.model.Score} (mandatory)
     */
    void updateScore(Score homeScore, Score awayScore);

    /**
     * Builds a list of in-progress matches ordered by their absolut score (descending order) and
     * then after start time(most recent started game first)
     * @return Ordered list of in-progress {@link MatchSummary}
     */
    List<MatchSummary> buildLiveMatchesSummary();
}
