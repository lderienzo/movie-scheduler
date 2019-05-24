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
	private ApplicationArguments args;
	private String moviesFilePath = "";
	private String hoursFilePath = "";
	private Hours hours;
	private Movies movies;


	public static void main(String[] args) {
		LOG.info("*** STARTING THE APPLICATION ***");
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
		LOG.info("*** APPLICATION FINISHED ***");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("*** BEGIN RUNNING APP ***");

		setArgs(args);
		if (argsWhereSet()) {
			loadHours();
			loadMovies();
		}
//		else
//			throw new MovieShowingSchedulerException("Unable to run application due to missing arguments.");

		LOG.info("*** END RUNNING APP ***");
	}

	private void setArgs(ApplicationArguments args) {
		this.args = args;
	}

	private boolean argsWhereSet() {
		return this.args != null && args.getSourceArgs().length == ArgOption.values().length;
	}

	private void loadHours() {
		getHoursFilePathFromCommandOption();
		createHoursObjectFromFile();
	}

	private void getHoursFilePathFromCommandOption() {
		if (filePathForHoursIsPresent()) {
			setHoursFilePathWithOptionValue();
		}
	}

	private boolean filePathForHoursIsPresent() {
		return filePathWasSpecified(HOURS_FILE.toString());
	}

	private boolean filePathWasSpecified(String optionName) {
		return containsOptionValueFor(optionName);
	}

	private boolean containsOptionValueFor(String optionName) {
		return args.containsOption(optionName);
	}

	private void setHoursFilePathWithOptionValue() {
		hoursFilePath = getFilePathFromOption(HOURS_FILE.toString());
	}

	private String getFilePathFromOption(String optionName) {
		return pathOptionValueFor(optionName);
	}

	private String pathOptionValueFor(String optionName) {
		return args.getOptionValues(optionName).get(0);
	}

	private void createHoursObjectFromFile() {
		try {
			createHoursObjectFromJsonFile();
		} catch (IOException e) {
			throw new MovieShowingSchedulerException(e.getMessage());
		}
	}

	private void createHoursObjectFromJsonFile() throws IOException {
		if (filePathIsValid(hoursFilePath)) {
			hours = JsonDeserializerForScheduler.getHours(hoursFilePath);
		}
	}

	private void loadMovies() {
		getMoviesFilePathFromCommandOption();
		createMoviesObjectFromFile();
	}

	private void getMoviesFilePathFromCommandOption() {
		if (filePathForMoviesIsPresent())
			setMoviesFilePathWithOptionValue();
	}

	private boolean filePathForMoviesIsPresent() {
		return filePathWasSpecified(MOVIE_FILE.toString());
	}

	private void setMoviesFilePathWithOptionValue() {
		moviesFilePath = getFilePathFromOption(MOVIE_FILE.toString());
	}

	private void createMoviesObjectFromFile() {
		try {
			createMoviesObjectFromJsonFile();
		} catch (IOException e) {
			throw new MovieShowingSchedulerException(e.getMessage());
		}
	}

	private void createMoviesObjectFromJsonFile() throws IOException {
		if (filePathIsValid(moviesFilePath)) {
			movies = JsonDeserializerForScheduler.getMovies(moviesFilePath);
		}
	}

	private boolean filePathIsValid(String path) {
		File file = new File(path);
		return file != null && file.isFile() && file.canRead();
	}
}
