package com.football.scoreboard.api.exception;

/**
 * Thrown to indicate that the provided score value is not a positive number
 */
public class InvalidScoreValueException extends RuntimeException {
    public InvalidScoreValueException(String message) {
        super(message);
    }
}
