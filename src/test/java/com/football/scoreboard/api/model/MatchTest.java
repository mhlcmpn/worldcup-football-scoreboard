package com.football.scoreboard.api.model;

import com.football.scoreboard.api.model.Match;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchTest {

    private static final String HOME_TEAM = "USA";
    private static final String AWAY_TEAM = "Austria";

    @Test
    public void testHomeTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Match(null, AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());

    }

    @Test
    public void testHomeTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Match(" ", AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testAwayTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Match(HOME_TEAM, null);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testAwayTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Match(HOME_TEAM, " ");
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testInitialScoreForStartedGameIs00() {
        Match match = new Match(HOME_TEAM, AWAY_TEAM);
        match.start();
        assertThat(match).isNotNull();
        assertThat(match.getHomeScore()).isZero();
        assertThat(match.getAwayScore()).isZero();
        assertThat(match.getStartTime()).isNotNull();
    }

    @Test
    void testMatchHashCodeAndEquals() {
        Match match1 = new Match("USA", "Hungary");
        Match match2 = new Match("UsA", "hungary");

        assertThat(match1.equals(match2)).isTrue();
        assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
    }
}
