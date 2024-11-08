package com.football.scoreboard.impl;


import com.football.scoreboard.api.model.MatchSummary;
import com.football.scoreboard.impl.model.Match;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchSummaryBuilderTest {

    @Test
    public void testMatchSummaryBuilding() {
        Match match = new Match("Austria", "Hungary");
        match.setHomeScore(1);
        match.setAwayScore(2);

        MatchSummary matchSummary = MatchSummaryBuilder.buildMatchSummary(match);
        assertThat(matchSummary).isNotNull();
        assertThat(matchSummary.getHomeTeam()).isEqualTo("Austria");
        assertThat(matchSummary.getHomeScore()).isEqualTo(1);
        assertThat(matchSummary.getAwayTeam()).isEqualTo("Hungary");
        assertThat(matchSummary.getAwayScore()).isEqualTo(2);
    }
}