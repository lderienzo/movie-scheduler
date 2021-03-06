package com.tandem.showtime.moviescheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tandem.showtime.moviescheduler.arguments.*;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;
import com.tandem.showtime.moviescheduler.schedule.*;


@SpringBootApplication
public class Application implements ApplicationRunner {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOG.info("*** BEGIN APPLICATION RUN ***");

		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);

		LOG.info("*** END APPLICATION RUN ***");
	}

	@Override
	public void run(ApplicationArguments args) {
		ApplicationData appData = ApplicationDataLoader.loadDataForArgs(args);
		Hours hours = appData.getHours();
		Movies movies = appData.getMovies();
		MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(hours, movies);
		movieScheduleGenerator.generateSchedules();
		writeSchedulesToFile(appData.getOutFilePath(),
							 movieScheduleGenerator.getWeekdaySchedule(),
							 movieScheduleGenerator.getWeekendSchedule());
	}

	private void writeSchedulesToFile(String outFilePath, Schedule weekdaySchedule, Schedule weekendSchedule) {
		LOG.info("Begin writing schedules to file...");
		new SchedulePdfWriter(weekdaySchedule, weekendSchedule).writeSchedules(outFilePath);
		LOG.info("...end writing schedules to file.");
	}
}
