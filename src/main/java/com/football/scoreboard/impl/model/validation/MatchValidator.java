package com.football.scoreboard.impl.model.validation;

import com.football.scoreboard.api.exception.MatchAlreadyStartedException;
import com.football.scoreboard.api.exception.NotFoundMatchException;
import com.football.scoreboard.impl.model.Match;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Validator for match status, teams names and scores
 */
public class MatchValidator {
    private static final Logger log = LoggerFactory.getLogger(MatchValidator.class);

    public static void validateTeamNames(String homeTeam, String awayTeam) {
        if (StringUtils.isAllBlank(homeTeam) || StringUtils.isAllBlank(awayTeam)) {
            throw new IllegalArgumentException("Both home team and away team are mandatory");
        }
        validateTeamNamesAreDifferent(homeTeam, awayTeam);
    }

    private static void validateTeamNamesAreDifferent(String homeTeam, String awayTeam) {
        if (StringUtils.compareIgnoreCase(homeTeam, awayTeam) == 0) {
            log.warn("Home team {} and away team {} have same value", homeTeam, awayTeam);
            throw new IllegalArgumentException("Home team and away team must have different values");
        }
    }

    public static void validateMatchWasNotAlreadyStarted(String homeTeam, String awayTeam, Match match) {
        if (Objects.nonNull(match)) {
            String message = String.format("Match %s - %s was already started", homeTeam, awayTeam);
            log.warn(message);
            throw new MatchAlreadyStartedException(message);
        }
    }

    public static void validateTheMatchIsInProgress(String homeTeam, String awayTeam, Match match) {
        if (Objects.isNull(match)) {
            String message = String.format("Match %s-%s is not in-progress", homeTeam, awayTeam);
            log.warn(message);
            throw new NotFoundMatchException(message);
        }
    }
}