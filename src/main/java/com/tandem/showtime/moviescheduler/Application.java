package com.tandem.showtime.moviescheduler;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itextpdf.text.DocumentException;

// TODO: reduce visibility of everything as much as possible. Make as much 'package-private' as possible.

@SpringBootApplication
public class Application implements ApplicationRunner {

	private MovieScheduleGenerator movieScheduleGenerator;
	private static Logger LOG = LoggerFactory.getLogger(Application.class);
	private ArgsProcessor argsProcessor;

	public static void main(String[] args) {
		LOG.info("*** STARTING APPLICATION ***");

		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);

		LOG.info("*** APPLICATION STOPPED ***");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("*** BEGIN RUNNING APP ***");

		argsProcessor = new ArgsProcessor(args);
		Hours hours = argsProcessor.getHours();
		Movies movies = argsProcessor.getMovies();
		movieScheduleGenerator = new MovieScheduleGenerator(hours, movies);
		movieScheduleGenerator.generateSchedules();
		writeSchedulesToFile(argsProcessor.getOutFilePath(), movieScheduleGenerator.getWeekdaySchedule(),
																movieScheduleGenerator.getWeekendSchedule());
		LOG.info("*** END RUNNING APP ***");
	}

	private void writeSchedulesToFile(String outFilePath, Schedule weekdaySchedule, Schedule weekendSchedule) {
		try {
			new SchedulePdfWriterService(weekdaySchedule, weekendSchedule).writeSchedules(outFilePath);
		} catch (FileNotFoundException | DocumentException e) {
			throw new MovieSchedulerException(e.getMessage());
		}
	}
}
