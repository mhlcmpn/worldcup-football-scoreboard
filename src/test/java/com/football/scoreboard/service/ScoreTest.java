package com.football.scoreboard.service;

import com.football.scoreboard.domain.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreTest {

    @Test
    public void testCannotCreateScoreWithNegativeValue() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score("USA", -1);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testCannotCreateScoreWithEmptyTeamName() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score(" ", 1);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    public void testCannotCreateScoreWithMissingName() {
        RuntimeException throwable = assertThrows(RuntimeException.class, () -> {
            new Score(null, 1);
        });
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }
}