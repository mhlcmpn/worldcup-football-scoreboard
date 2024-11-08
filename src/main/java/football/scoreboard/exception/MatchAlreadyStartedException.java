package football.scoreboard.exception;

public class MatchAlreadyStartedException extends RuntimeException {
    public MatchAlreadyStartedException(String message) {
        super(message);
    }
}