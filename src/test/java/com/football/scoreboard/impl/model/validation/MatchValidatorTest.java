package com.football.scoreboard.impl.model.validation;

import com.football.scoreboard.impl.model.Match;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchValidatorTest {

    private static final String HOME_TEAM = "USA";
    private static final String AWAY_TEAM = "Austria";

    @Test
    public void testHomeTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            MatchValidator.validateTeamNames(null, AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());

    }

    @Test
    public void testHomeTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            MatchValidator.validateTeamNames(" ", AWAY_TEAM);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testAwayTeamCannotBeNull() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            MatchValidator.validateTeamNames(HOME_TEAM, null);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testAwayTeamCannotBeEmpty() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            MatchValidator.validateTeamNames(HOME_TEAM, " ");
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
        Match match2 = new Match("USA", "Hungary");

        assertThat(match1.equals(match2)).isTrue();
        assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
    }

    @Test
    void testMatchHashCodeAndEqualsAreNotEquals() {
        Match match1 = new Match("USA", "Hungary");
        match1.start();
        match1.setHomeScore(1);
        match1.setAwayScore(2);

        shortSleepToHaveDifferentStartTimeForMatches(Duration.ofMillis(10));

        Match match2 = new Match("Usa", "hungary");
        match2.start();
        match2.setHomeScore(1);
        match2.setAwayScore(2);

        assertThat(match1.equals(match2)).isFalse();
        assertThat(match1.hashCode()).isNotEqualTo(match2.hashCode());
    }

    private void shortSleepToHaveDifferentStartTimeForMatches(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            //NOOP
        }
    }
}
