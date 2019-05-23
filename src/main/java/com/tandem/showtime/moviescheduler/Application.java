package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

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
	private Movies movies;


	public static void main(String[] args) {
		LOG.info("*** STARTING THE APPLICATION ***");
		new SpringApplication(Application.class);
		LOG.info("*** APPLICATION FINISHED ***");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		setArgs(args);
		if (argsWhereSet()) {
			loadMovies();
		}
	}

	private void setArgs(ApplicationArguments args) {
		this.args = args;
	}

	private boolean argsWhereSet() {
		return this.args != null;
	}

	private void loadMovies() {
		getFileNameFromCommandOption();
		createMoviesObjectFromFile();
	}

	private void getFileNameFromCommandOption() {
		if (filePathWasSpecified()) {
			readInPathOption();
		}
	}

	private boolean filePathWasSpecified() {
		return args.containsOption(MOVIE_FILE.toString());
	}

	private void readInPathOption() {
		moviesFilePath =  args.getOptionValues(MOVIE_FILE.toString()).get(0);
	}

	private void createMoviesObjectFromFile() {
		try {
			readJsonFileToCreateMoviesObject();
		} catch (IOException e) {
			throw new MovieShowingSchedulerException(e.getMessage());
		}
	}

	private void readJsonFileToCreateMoviesObject() throws IOException {
		if (pathToJsonMoviesFileIsValid()) {
			movies = JsonMovieListDeserializer.getMovies(moviesFilePath);
		}
	}

	private boolean pathToJsonMoviesFileIsValid() {
		return moviesFilePath != null && !moviesFilePath.isEmpty();
	}
}
