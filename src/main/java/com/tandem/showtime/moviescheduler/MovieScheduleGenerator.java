package com.tandem.showtime.moviescheduler;


import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MovieScheduleGenerator {
    private Movies movies;
    private boolean isWeekday;
    private Schedule weekdaySchedule;
    private Schedule weekendSchedule;
    private WeekdayHours weekdayHours;
    private WeekendHours weekendHours;
    private LocalTime currentTime;
    private LocalTime weekdayClosingTime;
    private LocalTime weekendClosingTime;
    private LocalTime startTimeForWeekdayShowings;
    private LocalTime whenLastWeekdayShowingCanEnd;
    private LocalTime startTimeForWeekendShowings;
    private LocalTime whenLastWeekendShowingCanEnd;
    private static final Logger LOG = LoggerFactory.getLogger(MovieScheduleGenerator.class);
    private static final int TIME_REQUIRED_FOR_PREVIEWS = 15;
    private static final int TIME_REQUIRED_FOR_THEATRE_PREP = 20;

    public MovieScheduleGenerator(Hours theatreHours, Movies moviesPlaying) {
        setArgs(moviesPlaying);
        setWeekdayHours(theatreHours);
        setWeekendHours(theatreHours);
        determineStartTimeForWeekdayShowings();
        determineWhenLastWeekdayShowingCanEnd();
        determineStartTimeForWeekendShowings();
        determineWhenLastWeekendShowingCanEnd();
    }

    private void setArgs(Movies moviesPlaying) {
        movies = moviesPlaying;
    }

    private void setWeekdayHours(Hours hours) {
        weekdayHours = hours.weekday();
    }

    private void setWeekendHours(Hours hours) {
        weekendHours = hours.weekend();
    }

    private void determineStartTimeForWeekdayShowings() {
        startTimeForWeekdayShowings = weekdayHours.startTimeForAllShowings();
    }

    private void determineWhenLastWeekdayShowingCanEnd() {
        weekdayClosingTime = determineClosingTimeForWeekdays();
        whenLastWeekdayShowingCanEnd = weekdayClosingTime;
    }

    private LocalTime determineClosingTimeForWeekdays() {
        return weekdayHours.closing();
    }

    private void determineStartTimeForWeekendShowings() {
        startTimeForWeekendShowings = weekendHours.startTimeForAllShowings();
    }

    private void determineWhenLastWeekendShowingCanEnd() {
        weekendClosingTime = determineClosingTimeForWeekends();
        whenLastWeekendShowingCanEnd = weekendClosingTime;
    }

    private LocalTime determineClosingTimeForWeekends() {
        return weekendHours.closing();
    }

    private Schedule determineMovieScheduleForWeekdayShowings() {
        isWeekday = true;
        return determineMovieScheduleForShowings(whenLastWeekdayShowingCanEnd, startTimeForWeekdayShowings, weekdayClosingTime);
    }

    public void generateSchedules() {
        LOG.info("Begin schedule generation...");
        weekdaySchedule = determineMovieScheduleForWeekdayShowings();
        weekendSchedule = determineMovieScheduleForWeekendShowings();
        LOG.info("...end schedule generation.");
    }

    private Schedule determineMovieScheduleForShowings(LocalTime whenLastShowingCanEnd,
                                                       LocalTime startTimeForAllShowings,
                                                       LocalTime closingTime) {

        Schedule schedule = new Schedule(movies.playing().size());
        for (Movie movie : movies.playing()) {
            currentTime = whenLastShowingCanEnd;
            while (true) {
                LocalTime showingEndingTime = determineShowingEndingTime(closingTime);
                LocalTime latestShowingCanStart = determineLatestTimeShowingCanStart(movie, showingEndingTime);
                if (latestTimeShowCanStartIsBeforeStartTimeForWeekdayShowings(latestShowingCanStart, startTimeForAllShowings)) {
                    break;
                }
                LocalTime scheduledStartTime = determineScheduledShowingStartTime(latestShowingCanStart);
                LocalTime scheduledEndTime = determineScheduledShowingEndTime(scheduledStartTime, movie.length());
                if (isWeekday)
                    movie.addWeekdayShowing(new Showing(scheduledStartTime, scheduledEndTime));
                else
                    movie.addWeekendShowing(new Showing(scheduledStartTime, scheduledEndTime));
                currentTime = determineStartTimeForRequiredPreviews(scheduledStartTime);
            }
            schedule.moviesPlaying().add(movie);
        }
        return schedule;
    }

    private boolean latestTimeShowCanStartIsBeforeStartTimeForWeekdayShowings
            (LocalTime latestTimeLastShowingCanStart, LocalTime startTimeForAllShowings) {
        return latestTimeLastShowingCanStart.compareTo(startTimeForAllShowings) < 0;
    }

    private LocalTime determineShowingEndingTime(LocalTime closingTime) {
        if (currentTime.getHourOfDay() == closingTime.getHourOfDay())
            return currentTime;
        else
            return currentTime.minusMinutes(TIME_REQUIRED_FOR_THEATRE_PREP);
    }

    private LocalTime determineLatestTimeShowingCanStart(Movie movie, LocalTime lastShowingEndingTime) {
        return lastShowingEndingTime.minusMinutes(movie.length());
    }

    private LocalTime determineScheduledShowingStartTime(LocalTime latestTimeLastShowingCanStart) {
        return makeStartTimeEasyToReadForSchedule(latestTimeLastShowingCanStart);
    }

    private LocalTime makeStartTimeEasyToReadForSchedule(LocalTime latestTimeLastShowingCanStart) {
        return ScheduleUtils.formatTimeForSchedule(latestTimeLastShowingCanStart);
    }

    private LocalTime determineScheduledShowingEndTime(LocalTime scheduledShowingStartTime, int movieLength) {
        return scheduledShowingStartTime.plusMinutes(movieLength);
    }

    private LocalTime determineStartTimeForRequiredPreviews(LocalTime scheduledShowingStartTime) {
        return scheduledShowingStartTime.minusMinutes(TIME_REQUIRED_FOR_PREVIEWS);
    }

    private Schedule determineMovieScheduleForWeekendShowings() {
        isWeekday = false;
        return determineMovieScheduleForShowings(whenLastWeekendShowingCanEnd, startTimeForWeekendShowings, weekendClosingTime);
    }

    public Schedule getWeekdaySchedule() {
        return weekdaySchedule;
    }

    public Schedule getWeekendSchedule() {
        return weekendSchedule;
    }
}
