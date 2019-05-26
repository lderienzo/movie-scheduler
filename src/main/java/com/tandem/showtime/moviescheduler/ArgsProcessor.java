package com.tandem.showtime.moviescheduler;


import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.SCHEDULE_FILE;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import com.google.common.base.Strings;

public class ArgsProcessor {
    private static Logger LOG = LoggerFactory.getLogger(ArgsProcessor.class);
    private ApplicationArguments args;
    private Hours hours = new Hours();
    private Movies movies = new Movies();
    private String outFilePath = "";    // TODO: double-check if this initialization is necessary. trying to avoid potential NPE.
    private String moviesFilePath = "";
    private String hoursFilePath = "";
    private String directoryPortionOfPath;
    private String hoursOption = HOURS_FILE.toString();
    private String moviesOption = MOVIE_FILE.toString();
    private String outFilePathOption = SCHEDULE_FILE.toString();
    private int expectedNumberOfArgs = ArgOption.values().length;
    private static final int INDEX_FOR_OPTION_VALUE = 1;
    private int indexForOptionNumber;

    public ArgsProcessor(ApplicationArguments args) {
        setArgs(args);
        if (argsWhereNotSet())
            throw new ArgsProcessorException("Unable to run application due closing missing arguments.");
        // TODO: print usage to console at this point
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
        loadHours();
        return hours;
    }

    public Movies getMovies() {
        loadMovies();
        return movies;
    }

    private void loadHours() {
        getHoursFilePathFromArgOption();
        createHoursObjectFromFile();
    }

    private void getHoursFilePathFromArgOption() {
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

    public String getOutFilePath() {
        indexForOptionNumber = 2;
        outFilePath = getFilePathFromOption(indexForOptionNumber);
        makeSureOutFilePathIsValid();
        return outFilePath;
    }

    private void makeSureOutFilePathIsValid() {
        if (outFilePath.contains(systemDependentDefaultNameSeparator())) {
            extractJustDirectoryPortionOfPath();
            determineIfDirectoryExists();
        }
    }

    private void extractJustDirectoryPortionOfPath() {
        int indexOfLastSeparatorChar = outFilePath.lastIndexOf(systemDependentDefaultNameSeparator());
        directoryPortionOfPath = outFilePath.substring(0,indexOfLastSeparatorChar);
    }

    private void determineIfDirectoryExists() {
        if (directoryDoesNotExist()) {
            clearOutFilePath();
            throw new ArgsProcessorException("Directory portion of path specified for 'schedule_file' is invalid. Please re-enter.");
        }
    }

    private boolean directoryDoesNotExist() {
        return !directoryExists();
    }

    private boolean directoryExists() {
        boolean directoryExists = false;
        if (pathIsNotEmpty()) {
            File file = new File(directoryPortionOfPath);
            directoryExists = file.isDirectory();
        }
        return directoryExists;
    }

    private boolean pathIsNotEmpty() {
        return !Strings.isNullOrEmpty(directoryPortionOfPath);
    }

    private void clearOutFilePath() {
        outFilePath = "";
    }

    private String systemDependentDefaultNameSeparator() {
        return File.separatorChar + "";
    }

    private boolean outFilePathIsPresent() {
        return filePathWasSpecified(outFilePathOption);
    }

    private void setOutFilePathWithOptionValue() {

        outFilePath = getFilePathFromOption(indexForOptionNumber);
    }

    private void setHoursFilePathWithOptionValue() {
        indexForOptionNumber = 0;
        hoursFilePath = getFilePathFromOption(indexForOptionNumber);
    }

    private String getFilePathFromOption(int indexForOptionNumber) {
        return pathOptionValueFor(indexForOptionNumber);
    }

    private String pathOptionValueFor(int indexForOptionNumber) {
        return args.getSourceArgs()[indexForOptionNumber]
                .split("=")[INDEX_FOR_OPTION_VALUE];
    }

    private void createHoursObjectFromFile() {
        try {
            createHoursObjectFromJsonFile();
        } catch (IOException e) {
            throw new MovieSchedulerException(e.getMessage());
        }
    }

    private void createHoursObjectFromJsonFile() throws IOException {
        if (filePathIsValid(hoursFilePath)) {
            hours = JsonDeserializerForScheduler.getHours(hoursFilePath);
        }
        else
            throw new ArgsProcessorException("Invalid file path for " + HOURS_FILE.toString());
    }

    private void loadMovies() {
        getMoviesFilePathFromArgOption();
        createMoviesObjectFromFile();
    }

    private void getMoviesFilePathFromArgOption() {
        if (filePathForMoviesIsPresent())
            setMoviesFilePathWithOptionValue();
    }

    private boolean filePathForMoviesIsPresent() {
        return filePathWasSpecified(moviesOption);
    }

    private void setMoviesFilePathWithOptionValue() {
        indexForOptionNumber = 1;
        moviesFilePath = getFilePathFromOption(indexForOptionNumber);
    }

    private void createMoviesObjectFromFile() {
        try {
            createMoviesObjectFromJsonFile();
        } catch (IOException e) {
            throw new MovieSchedulerException(e.getMessage());
        }
    }

    private void createMoviesObjectFromJsonFile() throws IOException {
        if (filePathIsValid(moviesFilePath)) {
            movies = JsonDeserializerForScheduler.getMovies(moviesFilePath);
        }
        else
            throw new ArgsProcessorException("Invalid file path for " + MOVIE_FILE.toString());
    }

    private boolean filePathIsValid(String path) {
        File file = new File(path);
        return file != null && file.isFile() && file.canRead();     // TODO: use isReadable(Path path)?
    }
}
