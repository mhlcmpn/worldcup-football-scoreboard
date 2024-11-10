package com.football.scoreboard.api.model;

import com.football.scoreboard.api.exception.InvalidScoreValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreTest {

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
}