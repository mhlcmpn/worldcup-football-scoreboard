package com.football.scoreboard.service;

import com.football.scoreboard.domain.Match;

import java.util.Comparator;

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