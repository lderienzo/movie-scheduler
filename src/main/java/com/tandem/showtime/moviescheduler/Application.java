package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

	@Autowired
	private MovieSchedulerService movieSchedulerService;
	private static Logger LOG = LoggerFactory.getLogger(Application.class);
	private ArgsProcessor argsProcessor;
	private ApplicationArguments args;
	private Hours hours = new Hours();
	private Movies movies = new Movies();

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

		LOG.info("*** END RUNNING APP ***");
	}
}
