package com.tandem.showtime.moviescheduler;

import org.joda.time.LocalTime;

// TODO: clean up and see if this can be made into a service and have Spring inject it
//@Service
public class MovieSchedulerService {

    private static final int TIME_REQUIRED_FOR_PREVIEWS = 15;
    private static final int TIME_REQUIRED_FOR_THEATRE_PREP = 20;
    private Hours hours;
    private Movies movies;
    private WeekdayHours weekdayHours;
    private WeekendHours weekendHours;
    private LocalTime currentTime;
    private LocalTime weekdayClosingTime;
    private LocalTime weekendClosingTime;
    private LocalTime startTimeForWeekdayShowings;
    private LocalTime whenLastWeekdayShowingCanEnd;
    private LocalTime startTimeForWeekendShowings;
    private LocalTime whenLastWeekendShowingCanEnd;
    private Schedule schedule;
    private boolean isWeekday;

    public MovieSchedulerService(Hours theatreHours, Movies moviesPlaying) {
        setArgs(theatreHours, moviesPlaying);
        setWeekdayHours(theatreHours);
        setWeekendHours(theatreHours);
        determineStartTimeForWeekdayShowings();
        determineWhenLastWeekdayShowingCanEnd();
        determineStartTimeForWeekendShowings();
        determineWhenLastWeekendShowingCanEnd();
    }

    private void setArgs(Hours theatreHours, Movies moviesPlaying) {
        hours = theatreHours;
        movies = moviesPlaying;
    }

    private void initializeSchedule() {
        schedule = new Schedule(forNumberOfMoviesPlaying());
    }

    private int forNumberOfMoviesPlaying() {
        return movies.playing().size();
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

    public Schedule determineMovieScheduleForWeekdayShowings() {
        isWeekday = true;
        initializeSchedule();
        return determineMovieScheduleForShowings(whenLastWeekdayShowingCanEnd, startTimeForWeekdayShowings, weekdayClosingTime);
    }

    private Schedule determineMovieScheduleForShowings(LocalTime whenLastShowingCanEnd, LocalTime startTimeForAllShowings, LocalTime closingTime) {

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

                // TODO: problem here. need to differentiate between weekday and weekend showings -- don't like this solution
                if (isWeekday) {
                    movie.addWeekdayShowing(new Showing(scheduledStartTime, scheduledEndTime));
                }
                else
                    movie.addWeekendShowing(new Showing(scheduledStartTime, scheduledEndTime));

                LocalTime startTimeForRequiredPreviews = determineStartTimeForRequiredPreviews(scheduledStartTime);

                currentTime = startTimeForRequiredPreviews;
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
        LocalTime scheduledShowingStartTime = makeStartTimeEasyToReadForSchedule(latestTimeLastShowingCanStart);
        return scheduledShowingStartTime;
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

    public Schedule determineMovieScheduleForWeekendShowings() {
        isWeekday = false;
        initializeSchedule();
        return determineMovieScheduleForShowings(whenLastWeekendShowingCanEnd, startTimeForWeekendShowings, weekendClosingTime);
    }
}
