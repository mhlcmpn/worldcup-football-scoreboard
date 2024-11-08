package com.football.scoreboard.api.service;

import com.football.scoreboard.api.exception.InvalidScoreValueException;
import com.football.scoreboard.api.exception.MatchAlreadyStartedException;
import com.football.scoreboard.api.model.Match;
import com.football.scoreboard.api.model.Score;

import java.util.List;

/**
 * ScoreboardService interface defines the public APi for handling in-progress matches scoreboard
 */
public interface ScoreboardService {

    /**
     * Create an entry on the scoreboard with provided home team and away team. Initial score is 0-0.
     * The method will throw {@link MatchAlreadyStartedException} if
     * the match has been started already
     * Constraints: homeTeam and awayTeams must have different values
     * @param homeTeam home team name (mandatory)
     * @param awayTeam away team name (mandatory)
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Find a match on the scoreboard. If not found, return null
     * @param homeTeam home team name (mandatory)
     * @param awayTeam away team name (mandatory)
     * @return In-progress match corresponding to given homeTeam and awayTeam names
     */
    Match findMatch(String homeTeam, String awayTeam);

    /**
     * Mark a match as finished and remove it from scoreboard. If the match is not on the scoreboard, it will throw
     * {@link MatchAlreadyStartedException}
     * @param homeTeam home team name (mandatory)
     * @param awayTeam away team name (mandatory)
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Update scores on the scoreboard. If the score value is not a positive number, it will throw
     * {@link InvalidScoreValueException}
     * @param homeScore {@link com.football.scoreboard.api.model.Score} (mandatory)
     * @param awayScore {@link com.football.scoreboard.api.model.Score} (mandatory)
     */
    void updateScore(Score homeScore, Score awayScore);

    /**
     * Builds a list of in-progress matches ordered by their absolut score (descending order) and
     * then after start time(most recent started game first)
     * @return Ordered list of in-progress {@link com.football.scoreboard.api.model.Match}
     */
    List<Match> buildLiveMatchesSummary();
}
