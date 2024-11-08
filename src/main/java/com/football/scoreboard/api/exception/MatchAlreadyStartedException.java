package com.football.scoreboard.api.exception;

/**
 * Thrown to indicate the that match was already started and added on the scoreboard
 */
public class MatchAlreadyStartedException extends RuntimeException {
    public MatchAlreadyStartedException(String message) {
        super(message);
    }
}