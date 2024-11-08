package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;
import com.football.scoreboard.exception.MatchAlreadyStartedException;
import org.junit.jupiter.api.Test;

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
}
