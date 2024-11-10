package com.football.scoreboard.impl;

import com.football.scoreboard.api.exception.MatchAlreadyStartedException;
import com.football.scoreboard.api.exception.NotFoundMatchException;
import com.football.scoreboard.api.model.MatchSummary;
import com.football.scoreboard.api.model.Score;
import com.football.scoreboard.impl.model.Match;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreboardServiceTest {

    private final String HOME_TEAM = "USA";
    private final String AWAY_TEAM = "Austria";

    private ScoreboardServiceImpl scoreboardService;

    @BeforeEach
    public void setup() {
        scoreboardService = new ScoreboardServiceImpl();
    }

    @Test
    public void testStartedMatchIsOnTheScoreboard() {
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        assertThat(scoreboardService.findMatch(HOME_TEAM, AWAY_TEAM)).isNotNull();
    }

    @Test
    public void testHomeTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch(null, AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboardService.findMatch(null, AWAY_TEAM)).isNull();
    }

    @Test
    public void testHomeTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch("  ", AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboardService.findMatch("  ", AWAY_TEAM)).isNull();
    }

    @Test
    public void testAwayTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch(HOME_TEAM, null);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboardService.findMatch(HOME_TEAM, null)).isNull();
    }

    @Test
    public void testAwayTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch(HOME_TEAM, " ");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboardService.findMatch(HOME_TEAM, " ")).isNull();
    }

    @Test
    public void testHomeTeamMustBeDifferentThanAwayTeam() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch("UsA", "usa");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboardService.findMatch(AWAY_TEAM, AWAY_TEAM)).isNull();
    }

    @Test
    public void testInitialScoreForStartedGameIs00() {
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        Match startedMatch = scoreboardService.findMatch(HOME_TEAM, AWAY_TEAM);
        assertThat(startedMatch).isNotNull();
        assertThat(startedMatch.getHomeScore()).isZero();
        assertThat(startedMatch.getAwayScore()).isZero();
    }

    @Test
    public void testThatAMatchCannotBeStartedMultipleTimes() {
        scoreboardService.startMatch(StringUtils.lowerCase(HOME_TEAM), StringUtils.upperCase(AWAY_TEAM));
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch(StringUtils.upperCase(HOME_TEAM), StringUtils.lowerCase(AWAY_TEAM));
        });
        assertEquals(MatchAlreadyStartedException.class, throwable.getClass());
    }

    @Test
    public void testFinishedMatchIsRemovedFromScoreboard() {
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        scoreboardService.finishMatch(HOME_TEAM, AWAY_TEAM);

        assertThat(scoreboardService.findMatch(null, AWAY_TEAM)).isNull();
    }

    @Test
    public void whenStartingTheSameMatchOnMultipleThreadsOnScoreboardItAppearsOnce() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Callable<Void> startMatchCallables = () -> {
            scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
            return null;
        };

        submitCallablesOnMultipleThreads(executor, Collections.nCopies(5, startMatchCallables));
        assertThat(scoreboardService.findMatch(HOME_TEAM, AWAY_TEAM)).isNotNull();
    }

    @Test
    public void whenStartingAndFinishingGamesInTheSameTimeScoreboardIsNotCorrupted() {
        List<ImmutablePair<String, String>> matchesToStartAndThenFinish = generateTeamPairs(10, "existing");
        startMatches(matchesToStartAndThenFinish);
        List<Callable<Void>> finishMatchCallables = prepareCallablesForFinishTasks(matchesToStartAndThenFinish);

        List<ImmutablePair<String, String>> matchesToStartOnly = generateTeamPairs(10, "new");
        List<Callable<Void>> startMatchCallables = prepareCallablesForStartTasks(matchesToStartOnly);

        ExecutorService executor = Executors.newFixedThreadPool(15);
        submitCallablesOnMultipleThreads(executor, finishMatchCallables);
        submitCallablesOnMultipleThreads(executor, startMatchCallables);
        matchesToStartAndThenFinish.forEach(pair ->
                assertThat(scoreboardService.findMatch(pair.left, pair.right)).isNull()
        );

        matchesToStartOnly.forEach(pair ->
                assertThat(scoreboardService.findMatch(pair.left, pair.right)).isNotNull()
        );
    }

    @Test
    public void testUpdateScoreForInProgressMatch() {
        Score homeScore = new Score(HOME_TEAM, 1);
        Score awayScore = new Score(AWAY_TEAM, 0);

        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        scoreboardService.updateScore(homeScore, awayScore);

        Match match = scoreboardService.findMatch(HOME_TEAM, AWAY_TEAM);
        assertThat(match).isNotNull();
        assertThat(match.getHomeScore()).isEqualTo(homeScore.getScore());
        assertThat(match.getAwayScore()).isEqualTo(awayScore.getScore());
    }

    @Test
    public void testUpdateScoreWithHomeScoreNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.updateScore(null, new Score(AWAY_TEAM, 1));
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testUpdateScoreWithAwayScoreNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.updateScore(new Score(HOME_TEAM, 1), null);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testUpdateScoreForFinishedMatch() {
        Score homeScore = new Score(HOME_TEAM, 1);
        Score awayScore = new Score(AWAY_TEAM, 0);

        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        scoreboardService.finishMatch(HOME_TEAM, AWAY_TEAM);
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.updateScore(homeScore, awayScore);
        });
        assertEquals(NotFoundMatchException.class, throwable.getClass());
    }

    @Test
    public void testLiveMatchesSummaryIsEmptyWhenAllMatchesAreFinished() {
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        scoreboardService.finishMatch(HOME_TEAM, AWAY_TEAM);
        Collection<MatchSummary> liveMatches = scoreboardService.buildLiveMatchesSummary();
        assertThat(liveMatches).isNotNull();
        assertThat(liveMatches.isEmpty()).isTrue();
    }

    @Test
    public void testLiveMatchesSummaryContainsInProgressMatch() {
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        Collection<MatchSummary> liveMatches = scoreboardService.buildLiveMatchesSummary();
        assertThat(liveMatches).isNotNull();
        assertThat(liveMatches.isEmpty()).isFalse();
        assertThat(liveMatches.stream().filter(it -> HOME_TEAM.equalsIgnoreCase(it.getHomeTeam()) && AWAY_TEAM.equalsIgnoreCase(it.getAwayTeam())).findAny()).isNotEmpty();
    }

    @Test
    public void testLiveMatchesSummaryIsOrderedByTotalScoreAndStartDate() {

        List<MatchSummary> expectedMatchesSummary = prepareDataAndReturnMatchesInExpectedOrder();
        List<MatchSummary> actualMatchesSummary = scoreboardService.buildLiveMatchesSummary();
        assertThat(actualMatchesSummary).isNotNull();
        assertThat(actualMatchesSummary.isEmpty()).isFalse();
        assertThat(actualMatchesSummary.size()).isEqualTo(expectedMatchesSummary.size());
        assertThat(actualMatchesSummaryEqualsToExpectedMatchesSummary(actualMatchesSummary, expectedMatchesSummary)).isTrue();
    }

    private boolean actualMatchesSummaryEqualsToExpectedMatchesSummary(List<MatchSummary> actualMatchesSummary, List<MatchSummary> expectedMatchesSummary) {
        boolean isEqual = true;
        for (int i = 0; i < actualMatchesSummary.size() && isEqual; i++) {
            MatchSummary actual = actualMatchesSummary.get(i);
            MatchSummary expected = expectedMatchesSummary.get(i);
            isEqual = haveSameDetails(actual, expected);
        }
        return isEqual;
    }

    private boolean haveSameDetails(MatchSummary actualSummary, MatchSummary expectedSummary) {
        return Objects.equals(actualSummary.getHomeTeam(), expectedSummary.getHomeTeam())
                && Objects.equals(actualSummary.getAwayTeam(), expectedSummary.getAwayTeam())
                && actualSummary.getHomeScore() == expectedSummary.getHomeScore()
                && actualSummary.getAwayScore() == expectedSummary.getAwayScore();
    }

    private List<MatchSummary> prepareDataAndReturnMatchesInExpectedOrder() {
        List<MatchSummary> matches = new ArrayList<>();

        startMatchAndUpdateScore(new Score("Mexico", 0), new Score("Canada", 5));
        startMatchAndUpdateScore(new Score("Spain", 10), new Score("Brazil", 2));
        startMatchAndUpdateScore(new Score("Germany", 2), new Score("France", 2));
        startMatchAndUpdateScore(new Score("Uruguay", 6), new Score("Italy", 6));
        startMatchAndUpdateScore(new Score("Argentina", 3), new Score("Australia", 1));

        matches.add(MatchSummaryBuilder.buildMatchSummary(scoreboardService.findMatch("Uruguay", "Italy")));
        matches.add(MatchSummaryBuilder.buildMatchSummary(scoreboardService.findMatch("Spain", "Brazil")));
        matches.add(MatchSummaryBuilder.buildMatchSummary(scoreboardService.findMatch("Mexico", "Canada")));
        matches.add(MatchSummaryBuilder.buildMatchSummary(scoreboardService.findMatch("Argentina", "Australia")));
        matches.add(MatchSummaryBuilder.buildMatchSummary(scoreboardService.findMatch("Germany", "France")));

        return matches;
    }

    private void startMatchAndUpdateScore(Score homeScore, Score awayScore) {
        try {
            scoreboardService.startMatch(homeScore.getTeam(), awayScore.getTeam());
            scoreboardService.updateScore(homeScore, awayScore);
            //added a small sleep so that the matches do not start in the same time
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //NOOP
        }
    }

    private List<Callable<Void>> prepareCallablesForFinishTasks(List<ImmutablePair<String, String>> teamNamePairs) {
        List<Callable<Void>> finishMatchTasks = new ArrayList<>();
        teamNamePairs.forEach(pair ->
                finishMatchTasks.add(() -> {
                    scoreboardService.finishMatch(pair.left, pair.right);
                    return null;
                }));
        return finishMatchTasks;
    }

    private List<Callable<Void>> prepareCallablesForStartTasks(List<ImmutablePair<String, String>> teamNamePairs) {
        List<Callable<Void>> finishMatchTasks = new ArrayList<>();
        teamNamePairs.forEach(pair ->
                finishMatchTasks.add(() -> {
                    scoreboardService.startMatch(pair.left, pair.right);
                    return null;
                }));
        return finishMatchTasks;
    }

    private List<ImmutablePair<String, String>> generateTeamPairs(int count, String prefix) {
        List<ImmutablePair<String, String>> namePairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            namePairs.add(new ImmutablePair<>(prefix + HOME_TEAM + i, prefix + AWAY_TEAM + i));
        }
        return namePairs;
    }

    private void startMatches(List<ImmutablePair<String, String>> teamPairs) {
        teamPairs.forEach(pair ->
                scoreboardService.startMatch(pair.left, pair.right)
        );
    }

    private void submitCallablesOnMultipleThreads(ExecutorService executor, List<Callable<Void>> matches) {
        List<Future<Void>> futures = new ArrayList<>();
        for (Callable<Void> match : matches) {
            Future<Void> future = executor.submit(match);
            futures.add(future);
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                if (!(e.getCause() instanceof MatchAlreadyStartedException)) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
