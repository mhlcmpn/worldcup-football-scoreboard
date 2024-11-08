package football.scoreboard;

import football.scoreboard.exception.MatchAlreadyStartedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {
    private static final Logger log =LoggerFactory.getLogger(Scoreboard.class);

    private ConcurrentHashMap<String, Match> liveMatches = new ConcurrentHashMap<>();

    public void startMatch(String homeTeam, String awayTeam) {
        validateTeamNamesAreDifferent(homeTeam, awayTeam);
        validateMatchWasNotAlreadyStarted(homeTeam, awayTeam);
        Match match = new Match(homeTeam, awayTeam);
        match.start();
        liveMatches.put(buildMatchKey(homeTeam, awayTeam), match);
    }

    private void validateMatchWasNotAlreadyStarted(String homeTeam, String awayTeam) {
        if (Objects.nonNull(liveMatches.get(buildMatchKey(homeTeam, awayTeam)))) {
            String message = String.format("Match %s - %s was already started", homeTeam, awayTeam);
            log.warn(message);
            throw new MatchAlreadyStartedException(message);
        }
    }

    private void validateTeamNamesAreDifferent(String homeTeam, String awayTeam) {
        if (StringUtils.compare(homeTeam, awayTeam) == 0) {
            throw new IllegalArgumentException("Home team and away team must have different values");
        }
    }

    public Match getMatch(String homeTeam, String awayTeam) {
        return liveMatches.get(buildMatchKey(homeTeam, awayTeam));
    }

    private String buildMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + awayTeam;
    }

}
