package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

import org.springframework.stereotype.Component;


// TODO:  refactor this somehow.
@Component
public class TestConstants {

    public static final String HOURS_JSON_FILE_NAME = "hours.json";
    public static final String MOVIES_JSON_FILE_NAME = "movies.json";
    public static final String TEST_MOVIES_JSON_FILE_NAME = "test-movies.json";

    public static String PATH_TO_JSON_HOURS_FILE =
            new FileUtils().getAbsolutePathOfJsonFile(HOURS_JSON_FILE_NAME);

    public static String PATH_TO_JSON_MOVIE_FILE =
            new FileUtils().getAbsolutePathOfJsonFile(MOVIES_JSON_FILE_NAME);

    public static String PATH_TO_JSON_TEST_MOVIE_FILE =
            new FileUtils().getAbsolutePathOfJsonFile(TEST_MOVIES_JSON_FILE_NAME);

    public static final String[] ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_MOVIE_FILE};

    public static final String[] TEST_ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE};
}
