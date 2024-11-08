package com.football.scoreboard.impl;

import com.football.scoreboard.impl.model.Match;

import java.util.Comparator;

/**
 * Comparator used in to oder the summary list of in-progress matches based on absolut score (descending)
 * and then based on start date (most recent first)
 */
public class MatchComparator implements Comparator<Match> {

    @Override
    public int compare(Match first, Match second) {

        int comparison = Integer.compare(first.getAbsolutScore(), second.getAbsolutScore());
        if (comparison != 0) {
            return -comparison;  //reverse order
        }

        return second.getStartTime().compareTo(first.getStartTime());
    }
}