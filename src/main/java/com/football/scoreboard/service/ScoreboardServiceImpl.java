package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;
import com.football.scoreboard.domain.Score;
import com.football.scoreboard.domain.Scoreboard;
import com.football.scoreboard.exception.MatchAlreadyFinishedException;
import com.football.scoreboard.exception.MatchAlreadyStartedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
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
        if (StringUtils.compareIgnoreCase(homeTeam, awayTeam) == 0) {
            log.warn("Home team {} and away team {} have same value", homeTeam, awayTeam);
            throw new IllegalArgumentException("Home team and away team must have different values");
        }
    }

    @Override
    public Match findMatch(String homeTeam, String awayTeam) {
        return scoreboard.findMatch(buildMatchKey(homeTeam, awayTeam));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        scoreboard.removeMatch(buildMatchKey(homeTeam, awayTeam));
    }

    @Override
    public void updateScore(Score homeScore, Score awayScore) {
        validateScoreAreNotNull(homeScore, awayScore);
        String matchKey = buildMatchKey(homeScore.getTeam(), awayScore.getTeam());
        Match match = scoreboard.findMatch(matchKey);
        validateTheMatchIsInProgress(homeScore.getTeam(), awayScore.getTeam(), match);
        match.setHomeScore(homeScore.getScore());
        match.setAwayScore(awayScore.getScore());
        scoreboard.updateMatch(matchKey, match);
    }

    @Override
    public Collection<Match> buildLiveMatchesSummary() {
        return scoreboard.getLiveMatches();
    }

    private void validateTheMatchIsInProgress(String homeTeam, String awayTeam, Match match) {
        if (Objects.isNull(match)) {
            String message = String.format("Match %s-%s is not in progress", homeTeam, awayTeam);
            log.warn(message);
            throw new MatchAlreadyFinishedException(message);
        }
    }

    private void validateScoreAreNotNull(Score homeScore, Score awayScore) {
        if (Objects.isNull(homeScore) || Objects.isNull(awayScore)) {
            log.warn("Home score and away score are both mandatory");
            throw new IllegalArgumentException("Home score and away score are mandatory");
        }
    }

    private String buildMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + awayTeam;
    }
}
