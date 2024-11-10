package com.football.scoreboard.impl;

import com.football.scoreboard.api.model.MatchSummary;
import com.football.scoreboard.impl.model.Match;

/**
 * Match summary builder from a given match
 */
public class MatchSummaryBuilder {
    public static MatchSummary buildMatchSummary(Match match) {
        MatchSummary matchSummary = new MatchSummary(match.getHomeTeam(), match.getAwayTeam());
        matchSummary.setHomeScore(match.getHomeScore());
        matchSummary.setAwayScore(match.getAwayScore());
        return matchSummary;
    }
}
