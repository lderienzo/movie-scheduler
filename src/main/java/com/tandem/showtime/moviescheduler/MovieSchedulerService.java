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
    private LocalTime currentTime;
    private LocalTime closingTime;
    private LocalTime startTimeForWeekdayShows;
    private LocalTime whenLastWeekdayShowCanEnd;
    private Schedule schedule;

    public MovieSchedulerService(Hours theatreHours, Movies moviesPlaying) {
        hours = theatreHours;
        movies = moviesPlaying;
        weekdayHours = hours.weekday();
        schedule = new Schedule(forNumberOfMoviesPlaying());
        startTimeForWeekdayShows = determineStartTimeForWeekdayShows();
        whenLastWeekdayShowCanEnd = determineWhenLastWeekdayShowCanEnd();
    }

    private int forNumberOfMoviesPlaying() {
        return movies.playing().size();
    }

    public Schedule determineMovieScheduleForWeekdayShows() {

        for (Movie movie : movies.playing()) {

            currentTime = whenLastWeekdayShowCanEnd;

            while (true) {

                LocalTime showingEndingTime = determineShowingEndingTime();

                LocalTime latestTimeShowingCanStart = determineLatestTimeShowingCanStart(movie, showingEndingTime);

                if (latestTimeShowCanStartIsBeforeStartTimeForWeekdayShows(latestTimeShowingCanStart)) {
                    break;
                }

                LocalTime scheduledShowingStartTime = determineScheduledShowingStartTime(latestTimeShowingCanStart);

                LocalTime scheduledShowingEndTime = determineScheduledShowingEndTime(scheduledShowingStartTime, movie.length());

                movie.addShowing(new Showing(scheduledShowingStartTime, scheduledShowingEndTime));

                LocalTime startTimeForRequiredPreviews = determineStartTimeForRequiredPreviews(scheduledShowingStartTime);

                currentTime = startTimeForRequiredPreviews;
            }

            schedule.get().add(movie);
        }
        return schedule;
    }

    private boolean latestTimeShowCanStartIsBeforeStartTimeForWeekdayShows
            (LocalTime latestTimeLastShowingCanStart) {
        return latestTimeLastShowingCanStart.compareTo(startTimeForWeekdayShows) < 0;
    }

    private LocalTime determineStartTimeForWeekdayShows() {
        return weekdayHours.startTimeForWeekdayShows();
    }

    private LocalTime determineWhenLastWeekdayShowCanEnd() {
        closingTime = determineClosingTimeForWeekdays();
        return closingTime;
    }

    private LocalTime determineClosingTimeForWeekdays() {
        return weekdayHours.closing();
    }

    private LocalTime determineShowingEndingTime() {    // TODO: must write version for weekend
        if (currentTime.getHourOfDay() == closingTime.getHourOfDay()) // 11PM
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

    // TODO: implement
    public Schedule determineMovieScheduleForWeekendShows() {
        return null;
    }
}
