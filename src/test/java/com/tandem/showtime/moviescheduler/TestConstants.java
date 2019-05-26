package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.SCHEDULE_FILE;


// TODO:  refactor this somehow.
public class TestConstants {

    private static final String HOURS_JSON_FILE_NAME = "hours.json";
    private static final String MOVIES_JSON_FILE_NAME = "movies.json";
    private static final String TEST_MOVIES_JSON_FILE_NAME = "test-movies.json";

    private static String PATH_TO_JSON_HOURS_FILE =
            new FileUtils().getAbsolutePathOfResourceFile(HOURS_JSON_FILE_NAME);

    private static String PATH_TO_JSON_TEST_MOVIE_FILE =
            new FileUtils().getAbsolutePathOfResourceFile(TEST_MOVIES_JSON_FILE_NAME);

    public static String PATH_TO_JSON_MOVIE_FILE =
            new FileUtils().getAbsolutePathOfResourceFile(MOVIES_JSON_FILE_NAME);

    public static final String PATH_TO_TEST_SCHEDULE_OUTPUT_FILE = "/Users/lderienzo/Downloads/movie_schedule.pdf";

    public static final String[] ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_MOVIE_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_MISSING_MOVIE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_MISSING_HOURS_PATH =
            {"--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_MISSING_OUT_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE};

    public static final String[] TEST_ARGS_INVALID_MOVIES_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIE_FILE.toString() + "=/some/invalid/path/bogus.file",
                    "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_INVALID_HOURS_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=/some/invalid/path/bogus.file",
                    "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_INVALID_SCHEDULE_OUTPUT_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "=/some/invalid/path/bogus.file"};
}
