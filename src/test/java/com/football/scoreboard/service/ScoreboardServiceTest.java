package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;
import com.football.scoreboard.domain.Score;
import com.football.scoreboard.exception.MatchAlreadyFinishedException;
import com.football.scoreboard.exception.MatchAlreadyStartedException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private ScoreboardService scoreboardService = new ScoreboardServiceImpl();

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
            scoreboardService.startMatch(AWAY_TEAM, AWAY_TEAM);
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
        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
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
    public void testUpdateScoreForFinishedMatch() {
        Score homeScore = new Score(HOME_TEAM, 1);
        Score awayScore = new Score(AWAY_TEAM, 0);

        scoreboardService.startMatch(HOME_TEAM, AWAY_TEAM);
        scoreboardService.finishMatch(HOME_TEAM, AWAY_TEAM);
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboardService.updateScore(homeScore, awayScore);
        });
        assertEquals(MatchAlreadyFinishedException.class, throwable.getClass());
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
