package football.scoreboard;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {

    private ConcurrentHashMap<String, Match> liveMatches = new ConcurrentHashMap<>();

    public void startMatch(String homeTeam, String awayTeam) {
        validateTeamNamesAreDifferent(homeTeam, awayTeam);
        Match match = new Match(homeTeam, awayTeam);
        match.start();
        liveMatches.put(buildMatchKey(homeTeam, awayTeam), match);
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
