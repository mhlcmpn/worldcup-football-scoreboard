package com.football.scoreboard.api.model;

import com.football.scoreboard.api.exception.InvalidScoreValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreTest {

    @Test
    public void testCannotCreateScoreWithNegativeValue() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score("USA", -1);
        });
        assertEquals(InvalidScoreValueException.class, throwable.getClass());
    }

    @Test
    public void testCannotCreateScoreWithEmptyTeamName() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score(" ", 1);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testCannotCreateScoreWithMissingTeamName() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score(null, 1);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    void tesScoreHashCodeAndEquals() {
        Score score1 = new Score("USA", 2);
        Score score2 = new Score("USA", 2);

        assertThat(score1.equals(score2)).isTrue();
        assertThat(score1.hashCode()).isEqualTo(score2.hashCode());
    }
}