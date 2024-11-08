package com.football.scoreboard;

import football.scoreboard.Match;
import football.scoreboard.Scoreboard;
import football.scoreboard.exception.MatchAlreadyStartedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreboardTest {

    @Test
    public void testStartedMatchIsOnTheScoreboard() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("USA", "Austria");
        assertThat(scoreboard.getMatch("USA", "Austria")).isNotNull();
    }

    @Test
    public void testHomeTeamCannotBeNull() {
        Scoreboard scoreboard = new Scoreboard();
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboard.startMatch(null, "Austria");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboard.getMatch(null, "Austria")).isNull();
    }

    @Test
    public void testHomeTeamCannotBeEmpty() {
        Scoreboard scoreboard = new Scoreboard();
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboard.startMatch("  ", "Austria");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboard.getMatch("  ", "Austria")).isNull();
    }

    @Test
    public void testHomeTeamMustBeDifferentThanAwayTeam() {
        Scoreboard scoreboard = new Scoreboard();
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboard.startMatch("Austria", "Austria");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
        assertThat(scoreboard.getMatch("Austria", "Austria")).isNull();
    }

    @Test
    public void testInitialScoreForStartedGameIs00() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("USA", "Austria");

        Match startedMatch = scoreboard.getMatch("USA", "Austria");
        assertThat(startedMatch).isNotNull();
        assertThat(startedMatch.getHomeScore()).isZero();
        assertThat(startedMatch.getAwayScore()).isZero();
    }

    @Test
    public void testThatAMatchCannotBeStartedMultipleTimes() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("USA", "Austria");
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            scoreboard.startMatch("USA", "Austria");
        });
        assertEquals(MatchAlreadyStartedException.class, throwable.getClass());
    }
}
