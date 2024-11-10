package com.football.scoreboard.impl.model;


import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MatchTest {

    @Test
    public void testMatchHashCodeAndEquals() {
        Match match1 = new Match("USA", "Hungary");
        Match match2 = new Match("USA", "Hungary");

        assertThat(match1.equals(match2)).isTrue();
        assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
    }

    @Test
    public void testMatchHashCodeAndEqualsAreNotEqualsBecauseTheyStartedAtDifferentTimes() {
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