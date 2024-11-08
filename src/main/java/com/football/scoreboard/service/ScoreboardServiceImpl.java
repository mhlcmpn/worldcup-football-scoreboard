package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;
import com.football.scoreboard.domain.Scoreboard;
import com.football.scoreboard.exception.MatchAlreadyStartedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ScoreboardServiceImpl implements ScoreboardService {

    private static final Logger log = LoggerFactory.getLogger(ScoreboardServiceImpl.class);

    private Scoreboard scoreboard = new Scoreboard();

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        validateTeamNamesAreDifferent(homeTeam, awayTeam);
        validateMatchWasNotAlreadyStarted(homeTeam, awayTeam);
        Match match = new Match(homeTeam, awayTeam);
        match.start();
        scoreboard.addMatch(buildMatchKey(homeTeam, awayTeam), match);
    }

    private void validateMatchWasNotAlreadyStarted(String homeTeam, String awayTeam) {
        if (Objects.nonNull(scoreboard.findMatch(buildMatchKey(homeTeam, awayTeam)))) {
            String message = String.format("Match %s - %s was already started", homeTeam, awayTeam);
            log.warn(message);
            throw new MatchAlreadyStartedException(message);
        }
    }

    private void validateTeamNamesAreDifferent(String homeTeam, String awayTeam) {
        if (StringUtils.compare(homeTeam, awayTeam) == 0) {
            throw new IllegalArgumentException("Home team and away team must have different values");
        }
    }

    @Override
    public Match findMatch(String homeTeam, String awayTeam) {
        return scoreboard.findMatch(buildMatchKey(homeTeam, awayTeam));
    }

    private String buildMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + awayTeam;
    }
}
