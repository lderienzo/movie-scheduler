package com.tandem.showtime.moviescheduler;


import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIES_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.SCHEDULE_FILE;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import com.google.common.base.Strings;

public class ArgsProcessor {

    private ApplicationArguments args;
    private Hours hours = new Hours();
    private Movies movies = new Movies();
    private String outFilePath = "";    // TODO: double-check if this initialization is necessary. trying to avoid potential NPE.
    private String moviesFilePath = "";
    private String hoursFilePath = "";
    private String directoryPortionOfPath;
    private String[] argOptionNameAndValue;
    private int indexOfOptionInArgList;
    private static final int INDEX_FOR_OPTION_VALUE = 1;
    private static final String INVALID_FILE_PATH_MESSAGE_PREFIX = "Invalid file path for ";
    private static final String USAGE_MESSAGE = "\nUsage:\n" + " \tcom.tandem.showtime.moviescheduler.Application " +
                                                    "--" + HOURS_FILE + "=<path_to_hours_json_file> " +
                                                    "--" + MOVIES_FILE + "=<path_to_movies_json_file> " +
                                                    "--" + SCHEDULE_FILE + "=<(may include directory path) new_file_name>\n";
    private static final Logger LOG = LoggerFactory.getLogger(ArgsProcessor.class);


    public ArgsProcessor(ApplicationArguments args) {
        LOG.info("*** BEGIN ARGS PROCESSING ***");
        if (allArgsPresent(args))
            this.args = args;
        else {
            printUsageToConsole();
            throw new ArgsProcessorException("Unable to run application due to missing arguments.");
        }
        LOG.info("*** END ARGS PROCESSING ***");
    }

    private boolean allArgsPresent(ApplicationArguments args) {
        return args != null && args.containsOption(HOURS_FILE.toString()) &&
                args.containsOption(MOVIES_FILE.toString()) &&
                args.containsOption(SCHEDULE_FILE.toString());
    }

    private void printUsageToConsole() {
        System.out.println(USAGE_MESSAGE);
    }

    public Hours getHours() {
        // TODO: this smells of a single responsibility principle violation. arg processing should not
        // involve loading files, it should just parse out file path info and pass to something else to load the file.
        loadHours();
        return hours;
    }

    public Movies getMovies() {
        loadMovies();
        return movies;
    }

    public String getOutFilePath() {
        setOutFilePath();
        return outFilePath;
    }

    private void loadHours() {
        checkIfHoursFilePathValueIsPresent(HOURS_FILE);
        checkIfHoursFilePathValueIsValid();
        setHoursFilePathWithValue();
        createHoursObjectFromJsonFile();
    }

    private void checkIfHoursFilePathValueIsPresent(ArgOption argOption) {
        indexOfOptionInArgList = 0;
        checkIfOptionValueIsPresent(argOption);
    }

    private void checkIfOptionValueIsPresent(ArgOption argOption) {
        argOptionNameAndValue = splitOptionNameAndValue(indexOfOptionInArgList);
        if (optionValueIsMissing()) {
            printUsageToConsole();
            throw new ArgsProcessorException("Missing option value for "+ argOption);
        }
    }

    private String[] splitOptionNameAndValue(int optionIndex) {
        return args.getSourceArgs()[optionIndex].split("=");
    }

    private boolean optionValueIsMissing() {
        return argOptionNameAndValue.length != 2;
    }

    public void checkIfHoursFilePathValueIsValid() {
        if (filePathIsNotValid(argOptionNameAndValue[INDEX_FOR_OPTION_VALUE])) {
            printUsageToConsole();
            throw new ArgsProcessorException(INVALID_FILE_PATH_MESSAGE_PREFIX + HOURS_FILE.toString());
        }
    }

    private boolean filePathIsNotValid(String path) {
        return !filePathIsValid(path);
    }

    private boolean filePathIsValid(String path) {
        File file = new File(path);
        return file != null && file.isFile() && file.canRead();     // TODO: use isReadable(Path path)?
    }

    private void setHoursFilePathWithValue() {
        hoursFilePath = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE];
    }

    private void createHoursObjectFromJsonFile() {
        deserializeHoursFileContents();
    }

    private void deserializeHoursFileContents() {
        try {
            hours = JsonDeserializerForScheduler.getHours(hoursFilePath);
        }
        catch(IOException e) {
            throw new ArgsProcessorException(e.getMessage());
        }
    }

    private void loadMovies() {
        checkIfMoviesFilePathValueIsPresent(MOVIES_FILE);
        checkIfMoviesFilePathValueIsValid();
        setMoviesFilePathWithValue();
        createMoviesObjectFromJsonFile();
    }

    private void checkIfMoviesFilePathValueIsPresent(ArgOption argOption) {
        indexOfOptionInArgList = 1;
        checkIfOptionValueIsPresent(argOption);
    }

    private void checkIfMoviesFilePathValueIsValid() {
        if (filePathIsNotValid(argOptionNameAndValue[INDEX_FOR_OPTION_VALUE])) {
            printUsageToConsole();
            throw new ArgsProcessorException(INVALID_FILE_PATH_MESSAGE_PREFIX + HOURS_FILE.toString());
        }
    }

    private void setMoviesFilePathWithValue() {
        moviesFilePath = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE];
    }

    private void createMoviesObjectFromJsonFile() {
            deserializeMovieFileContents();
    }

    private void deserializeMovieFileContents() {
        try {
            movies = JsonDeserializerForScheduler.getMovies(moviesFilePath);
        } catch (IOException e) {
            throw new ArgsProcessorException(e.getMessage());
        }
    }

    private void setOutFilePath() {
        checkIfOutFileOptionValueIsPresent(SCHEDULE_FILE);
        checkIfOutFilePathValueIsValid();
        setOutFilePathWithValue();
    }

    private void checkIfOutFileOptionValueIsPresent(ArgOption argOption) {
        indexOfOptionInArgList = 2;
        checkIfOptionValueIsPresent(argOption);
    }

    private void checkIfOutFilePathValueIsValid() {
        if (argOptionNameAndValue[INDEX_FOR_OPTION_VALUE].contains(systemDependentDefaultNameSeparator())) {
            extractDirectoryPortionOfPath();
            determineIfDirectoryIsValid();
        }
//        else
//            throw new ArgsProcessorException("The specified file path for " + SCHEDULE_FILE + " contains the incorrect system name separator symbol.");
    }

    private String systemDependentDefaultNameSeparator() {
        return File.separatorChar + "";
    }

    private void extractDirectoryPortionOfPath() {
        int indexOfLastSeparatorChar = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE].lastIndexOf(systemDependentDefaultNameSeparator());
        directoryPortionOfPath = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE].substring(0,indexOfLastSeparatorChar);
    }

    private void determineIfDirectoryIsValid() {
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

    private void setOutFilePathWithValue() {
        outFilePath = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE];
    }
}
