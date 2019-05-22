package com.tandem.showtime.moviescheduler;


public class WeekdayHours {
    private static final int TIME_REQURIED_TILL_FIRST_SHOWING = 15;
    private int startTimeForWeekdayShows;

    public int getStartTimeForWeekdayShows() {
        int openingTime = getOpeningTime();
        startTimeForWeekdayShows = deriveStartTimeFromOpeningTime(openingTime);
        return startTimeForWeekdayShows;
    }

    private int getOpeningTime() {
        return 0;
    }

    private int deriveStartTimeFromOpeningTime(int openingTime) {
        return openingTime + TIME_REQURIED_TILL_FIRST_SHOWING;
    }

    public int getClosingTime() {
        return 0;
    }
}
