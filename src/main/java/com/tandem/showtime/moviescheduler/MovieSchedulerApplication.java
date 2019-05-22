package com.tandem.showtime.moviescheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieSchedulerApplication {
	private static final int TIME_REQUIRED_FOR_PREVIEWS = 15;
	private static final int TIME_REQUIRED_FOR_THEATRE_PREP = 20;

	public static void main(String[] args) {
		SpringApplication.run(MovieSchedulerApplication.class, args);
	}

	private Schedule determineMovieScheduleForWeekdayShows(Movies movies, HoursOfOperation hoursOfOperation) {
		WeekdayHours weekdayHours = hoursOfOperation.getWeekdayHours();
		int startTimeForWeekdayShows = weekdayHours.getStartTimeForWeekdayShows();
		int verylastShowingEndTime = determineVeryLastShowingEndingTime(weekdayHours);
		int currentTime = verylastShowingEndTime;
		Schedule schedule = new Schedule(5);

		for (Movie movie : movies.get()) {	// for each movie in list
			while (true) { 	// find optimal beginning and end times (given constraints)
				Showing showing = new Showing();
				int lastShowingEndingTime = determineLastShowingEndingTime(currentTime);
				int latestTimeLastShowingCanStart = determineLatestTimeLastShowingCanStart(showing, lastShowingEndingTime);
				if (latestTimeLastShowingCanStart < startTimeForWeekdayShows) {
					break;
				}
				int scheduledShowingStartTime = determineScheduledShowingStartTime(latestTimeLastShowingCanStart);
				showing.setStartTime(scheduledShowingStartTime);
				int scheduledShowingEndTime = determineScheduledShowingEndTime(scheduledShowingStartTime, movie.length());
				showing.setEndTime(scheduledShowingEndTime);
				schedule.get().add(showing);
				int startTimeForRequiredPreviews = determineStartTimeForRequiredPreviews(scheduledShowingStartTime);
				currentTime = startTimeForRequiredPreviews;
			}
		}

		return schedule;
	}

	private int determineVeryLastShowingEndingTime(WeekdayHours weekdayHours) {
		int closingTime = determineClosingTimeForWeekdays(weekdayHours);
		return closingTime;
	}

	// Create WeekdayHours Class
	private int determineClosingTimeForWeekdays(WeekdayHours weekdayHours) {
		return weekdayHours.getClosingTime();
	}

	private int determineLastShowingEndingTime(int currentTime) {
		if (currentTime == 11) // 11PM
			return currentTime;
		else
			return currentTime - TIME_REQUIRED_FOR_THEATRE_PREP;
	}

	private int determineLatestTimeLastShowingCanStart(Showing showing, int lastShowingEndingTime) {
		return lastShowingEndingTime - showing.getMovie().length();
	}

	private int determineScheduledShowingStartTime(int latestTimeLastShowingCanStart) {
		int scheduledShowingStartTime = makeStartTimeEasyToReadForSchedule(latestTimeLastShowingCanStart);
		return scheduledShowingStartTime;
	}

	private int makeStartTimeEasyToReadForSchedule(int latestTimeLastShowingCanStart) {
		int scheduledShowingStartTime = 0;
		// TODO: return some magic to round down to nearest 5 minute increment to return scheduledShowingStartTime
		return scheduledShowingStartTime;
	}

	private int determineScheduledShowingEndTime(int scheduledShowingStartTime, int movieLength) {
		return scheduledShowingStartTime + movieLength;
	}

	private int determineStartTimeForRequiredPreviews(int scheduledShowingStartTime) {
		return scheduledShowingStartTime - TIME_REQUIRED_FOR_PREVIEWS;
	}
}
