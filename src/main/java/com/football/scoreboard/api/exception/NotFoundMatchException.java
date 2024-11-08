package com.football.scoreboard.api.exception;

/**
 * Thrown to indicate that the match was not found on the scoreboard
 */
public class NotFoundMatchException extends RuntimeException {
    public NotFoundMatchException(String message) {
        super(message);
    }
}