package com.football.scoreboard.impl;

import com.football.scoreboard.api.model.MatchSummary;
import com.football.scoreboard.api.model.Score;
import com.football.scoreboard.api.service.ScoreboardService;
import com.football.scoreboard.impl.model.Match;
import com.football.scoreboard.impl.model.Scoreboard;
import com.football.scoreboard.impl.model.validation.MatchValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link com.football.scoreboard.api.service.ScoreboardService}
 */
public class ScoreboardServiceImpl implements ScoreboardService {

    private static final Logger log = LoggerFactory.getLogger(ScoreboardServiceImpl.class);

    private final Scoreboard scoreboard = new Scoreboard();

    @Override
    public void startMatch(String homeTeam, String awayTeam) {

        MatchValidator.validateTeamNames(homeTeam, awayTeam);
        String matchKey = buildMatchKey(homeTeam, awayTeam);
        MatchValidator.validateMatchWasNotAlreadyStarted(homeTeam, awayTeam, scoreboard.findMatch(matchKey));

        Match match = new Match(homeTeam, awayTeam);
        match.start();
        scoreboard.addMatch(matchKey, match);
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
        MatchValidator.validateTheMatchIsInProgress(homeScore.getTeam(), awayScore.getTeam(), match);
        match.setHomeScore(homeScore.getScore());
        match.setAwayScore(awayScore.getScore());
        scoreboard.updateMatch(matchKey, match);
    }

    @Override
    public List<MatchSummary> buildLiveMatchesSummary() {
        Collection<Match> liveMatches = scoreboard.getLiveMatches();
        if (liveMatches.isEmpty()) {
            return List.of();
        }
        return liveMatches.stream().sorted(new MatchComparator())
                .map(MatchSummaryBuilder::buildMatchSummary)
                .collect(Collectors.toList());
    }

    private String buildMatchKey(String homeTeam, String awayTeam) {
        return StringUtils.upperCase(homeTeam) + StringUtils.upperCase(awayTeam);
    }

    Match findMatch(String homeTeam, String awayTeam) {
        return scoreboard.findMatch(buildMatchKey(homeTeam, awayTeam));
    }

    private void validateScoreAreNotNull(Score homeScore, Score awayScore) {
        if (Objects.isNull(homeScore) || Objects.isNull(awayScore)) {
            log.warn("Home score and away score are both mandatory");
            throw new IllegalArgumentException("Home score and away score are mandatory");
        }
    }
}
