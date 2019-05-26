package com.tandem.showtime.moviescheduler;


import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

public class ArgsProcessor {
    private static Logger LOG = LoggerFactory.getLogger(ArgsProcessor.class);
    private ApplicationArguments args;
    private Hours hours = new Hours();
    private Movies movies = new Movies();
    private String moviesFilePath = "";
    private String hoursFilePath = "";
    private String hoursOption = HOURS_FILE.toString();
    private String moviesOption = MOVIE_FILE.toString();
    private int expectedNumberOfArgs = ArgOption.values().length;
    private int indexForOptionValue = 1;
    private int indexForOptionNumber;

    public ArgsProcessor(ApplicationArguments args) {
        setArgs(args);
        if (argsWhereNotSet())
            throw new ArgsProcessorException("Unable closing run application due closing missing arguments.");
    }

    private void setArgs(ApplicationArguments args) {
        this.args = args;
    }

    private boolean argsWhereNotSet() {
        return !argsWhereSet();
    }

    private boolean argsWhereSet() {
        return this.args != null && args.getSourceArgs().length == expectedNumberOfArgs;
    }

    public Hours getHours() {
        LOG.info("*** BEGIN PROCESSING HOURS ***");
        loadHours();
        LOG.info("*** END PROCESSING HOURS ***");
        return hours;
    }

    public Movies getMovies() {
        LOG.info("*** BEGIN PROCESSING MOVIES ***");
        loadMovies();
        LOG.info("*** END PROCESSING MOVIES ***");
        return movies;
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
        return filePathWasSpecified(hoursOption);
    }

    private boolean filePathWasSpecified(String optionName) {
        return containsOptionValueFor(optionName);
    }

    private boolean containsOptionValueFor(String optionName) {
        return args.containsOption(optionName);
    }

    private void setHoursFilePathWithOptionValue() {
        indexForOptionNumber = 0;
        hoursFilePath = getFilePathFromOption(hoursOption);
    }

    private String getFilePathFromOption(String optionName) {
        return pathOptionValueFor(optionName);
    }

    private String pathOptionValueFor(String optionName) {
        return args.getSourceArgs()[indexForOptionNumber]
                .split("=")[indexForOptionValue];
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
        return filePathWasSpecified(moviesOption);
    }

    private void setMoviesFilePathWithOptionValue() {
        indexForOptionNumber = 1;
        moviesFilePath = getFilePathFromOption(moviesOption);
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
