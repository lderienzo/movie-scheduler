package com.tandem.showtime.moviescheduler.arguments;


import static com.tandem.showtime.moviescheduler.arguments.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.MOVIES_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.SCHEDULE_FILE;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import com.google.common.base.Strings;
import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.schedule.JsonDeserializerForScheduler;
import com.tandem.showtime.moviescheduler.movie.Movies;

public class ArgsProcessor {
    private String error;
    private String outFilePath;
    private String moviesFilePath;
    private String hoursFilePath;
    private ApplicationArguments args;
    private Hours hours = new Hours();
    private Movies movies = new Movies();
    private int indexOfOptionInArgList;
    private String directoryPortionOfPath;
    private String[] argOptionNameAndValue;
    private static final int INDEX_FOR_OPTION_VALUE = 1;
    private static final Logger LOG = LoggerFactory.getLogger(ArgsProcessor.class);
    private static final String ERROR = "ERROR >>> ";
    private static final String INVALID_FILE_PATH_ERROR_MSG_PREFIX = ERROR + "Invalid file path for ";
    private static final String USAGE_MESSAGE = "\nUsage:\n" + " \tcom.tandem.showtime.moviescheduler.Application " +
                                                    "--" + HOURS_FILE + "=<path_to_hours_json_file> " +
                                                    "--" + MOVIES_FILE + "=<path_to_movies_json_file> " +
                                                    "--" + SCHEDULE_FILE + "=<(may include directory path) new_file_name>\n";

    public ArgsProcessor(ApplicationArguments args) {
        LOG.info("Begin argument processing...");
        if (allArgsPresent(args))
            this.args = args;
        else {
            printUsageToConsole();
            logError(ERROR + " missing required arguments.");
            throw new ArgsProcessorException(error);
        }
        LOG.info("...end argument processing.");
    }

    private void logError(String errorMsg) {
        setErrorMemberWithErrorMessage(errorMsg);
        LOG.error(error);
    }

    private void setErrorMemberWithErrorMessage(String errorMsg) {
        error = errorMsg;
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
        loadHours();  // TODO: this smells of a single responsibility principle violation.
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
            logError(ERROR + "missing required value for "+ argOption);
            throw new ArgsProcessorException(error);
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
            logError(INVALID_FILE_PATH_ERROR_MSG_PREFIX + HOURS_FILE.toString());
            throw new ArgsProcessorException(error);
        }
    }

    private boolean filePathIsNotValid(String path) {
        return !filePathIsValid(path);
    }

    private boolean filePathIsValid(String path) {
        File file = new File(path);
        return file.isFile() && file.canRead();
    }

    private void setHoursFilePathWithValue() {
        hoursFilePath = argOptionNameAndValue[INDEX_FOR_OPTION_VALUE];
    }

    private void createHoursObjectFromJsonFile() {
        try {
            deserializeHoursFileContents();
        } catch(IOException e) {
            logError(e.getMessage());
            throw new ArgsProcessorException(error);
        }
    }

    private void deserializeHoursFileContents() throws IOException {
        hours = JsonDeserializerForScheduler.getHours(hoursFilePath);
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
            logError(INVALID_FILE_PATH_ERROR_MSG_PREFIX + HOURS_FILE.toString());
            throw new ArgsProcessorException(error);
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
            logError(e.getMessage());
            throw new ArgsProcessorException(error);
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
            printUsageToConsole();
            logError(ERROR + " invalid directory for " + SCHEDULE_FILE.toString());
            throw new ArgsProcessorException(error);
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
