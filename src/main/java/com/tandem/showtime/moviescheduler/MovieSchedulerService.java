package com.tandem.showtime.moviescheduler;

import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;

@Service
public class MovieSchedulerService {

    private static final int TIME_REQUIRED_FOR_PREVIEWS = 15;
    private static final int TIME_REQUIRED_FOR_THEATRE_PREP = 20;
    private static final int NUMBER_OF_MOVIES_SHOWING = 5;
    private Hours hours;
    private Movies movies;
    private WeekdayHours weekdayHours;
    private LocalTime currentTime;
    private LocalTime closingTime;

    public MovieSchedulerService(Hours hours, Movies movies) {
        this.hours = hours;
        this.movies = movies;
        this.weekdayHours = hours.weekday();
    }


    public Schedule determineMovieScheduleForWeekdayShows() {

        LocalTime startTimeForWeekdayShows = determineStartTimeForWeekdayShows();
        LocalTime verylastShowingEndTime = determineVeryLastShowingEndingTime();
        currentTime = verylastShowingEndTime;
        Schedule schedule = new Schedule(NUMBER_OF_MOVIES_SHOWING);

        for (Movie movie : movies.playing()) {
            while (true) { 	// find optimal beginning and end times (given constraints)
                Showing showing = new Showing();
                LocalTime lastShowingEndingTime = determineLastShowingEndingTime();
                LocalTime latestTimeLastShowingCanStart = determineLatestTimeLastShowingCanStart(showing, lastShowingEndingTime);
                if (latestTimeLastShowCanStartIsEarlierThanStartTimeForWeekdayShows
                        (latestTimeLastShowingCanStart, startTimeForWeekdayShows)) {
                    break;
                }
                LocalTime scheduledShowingStartTime = determineScheduledShowingStartTime(latestTimeLastShowingCanStart);
//                showing.setStartTime(scheduledShowingStartTime);
//                int scheduledShowingEndTime = determineScheduledShowingEndTime(scheduledShowingStartTime, Integer.valueOf(movie.length()));
//                showing.setEndTime(scheduledShowingEndTime);
//                schedule.get().add(showing);
//                int startTimeForRequiredPreviews = determineStartTimeForRequiredPreviews(scheduledShowingStartTime);
//                currentTime = startTimeForRequiredPreviews;
            }
        }

        return schedule;
    }

    private boolean latestTimeLastShowCanStartIsEarlierThanStartTimeForWeekdayShows
            (LocalTime latestTimeLastShowingCanStart, LocalTime startTimeForWeekdayShows) {
        return latestTimeLastShowingCanStart.compareTo(startTimeForWeekdayShows) < 0;
    }

    private LocalTime determineStartTimeForWeekdayShows() {
        return weekdayHours.startTimeForWeekdayShows();
    }

    private LocalTime determineVeryLastShowingEndingTime() {
        closingTime = determineClosingTimeForWeekdays();
        return closingTime;
    }

    private LocalTime determineClosingTimeForWeekdays() {
        return weekdayHours.closing();
    }

    private LocalTime determineLastShowingEndingTime() {    // TODO: must write version for weekend
        if (currentTime.getHourOfDay() == closingTime.getHourOfDay()) // 11PM
            return currentTime;
        else
            return currentTime.minusMinutes(TIME_REQUIRED_FOR_THEATRE_PREP);
    }

    private LocalTime determineLatestTimeLastShowingCanStart(Showing showing, LocalTime lastShowingEndingTime) {
        return lastShowingEndingTime.minusMinutes(showing.getMovie().length());
    }

    private LocalTime determineScheduledShowingStartTime(LocalTime latestTimeLastShowingCanStart) {
        LocalTime scheduledShowingStartTime = makeStartTimeEasyToReadForSchedule(latestTimeLastShowingCanStart);
        return scheduledShowingStartTime;
    }

    private LocalTime makeStartTimeEasyToReadForSchedule(LocalTime latestTimeLastShowingCanStart) {
        return ScheduleUtils.formatTimeForSchedule(latestTimeLastShowingCanStart);
    }

    private boolean minutesAreNotAnIncrementOfFive(LocalTime latestTimeLastShowingCanStart) {
        return latestTimeLastShowingCanStart.getMinuteOfHour() % 5 != 0;
    }

    private int determineScheduledShowingEndTime(int scheduledShowingStartTime, int movieLength) {
        return scheduledShowingStartTime + movieLength;
    }

    private int determineStartTimeForRequiredPreviews(int scheduledShowingStartTime) {
        return scheduledShowingStartTime - TIME_REQUIRED_FOR_PREVIEWS;
    }
}
