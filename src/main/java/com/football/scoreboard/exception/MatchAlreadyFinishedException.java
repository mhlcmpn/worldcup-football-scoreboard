package com.football.scoreboard.exception;

public class MatchAlreadyFinishedException extends RuntimeException {
    public MatchAlreadyFinishedException(String message) {
        super(message);
    }
}