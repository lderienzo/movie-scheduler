package com.tandem.showtime.moviescheduler.utils;

import static com.tandem.showtime.moviescheduler.arguments.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.MOVIES_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.SCHEDULE_FILE;

public class TestConstants {

    private static final String HOURS_JSON_FILE_NAME = "hours.json";
    private static final String MOVIES_JSON_FILE_NAME = "movies.json";
    private static final String TEST_MOVIES_JSON_FILE_NAME = "test-movies.json";

    private static final String PATH_TO_JSON_HOURS_FILE =
            new TestFileUtils().getAbsolutePathOfResourceFile(HOURS_JSON_FILE_NAME);

    private static final String PATH_TO_JSON_TEST_MOVIE_FILE =
            new TestFileUtils().getAbsolutePathOfResourceFile(TEST_MOVIES_JSON_FILE_NAME);

    public static final String PATH_TO_JSON_MOVIE_FILE =
            new TestFileUtils().getAbsolutePathOfResourceFile(MOVIES_JSON_FILE_NAME);

    public static final String SCHEDULE_OUT_FILE_NAME = "movie_schedule.pdf";

    public static final String PATH_TO_TEST_SCHEDULE_OUTPUT_FILE = "/Users/lderienzo/Downloads/" + SCHEDULE_OUT_FILE_NAME;

    public static final String[] ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_MOVIE_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
            "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_WITH_JUST_OUT_FILE_NAME =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "=" + SCHEDULE_OUT_FILE_NAME};

    public static final String[] TEST_ARGS_INVALID_MOVIES_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIES_FILE.toString() + "=/some/invalid/path/bogus.file",
                    "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_INVALID_HOURS_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=/some/invalid/path/bogus.file",
                    "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "=" + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_INVALID_SCHEDULE_OUTPUT_FILE_PATH =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "=/some/invalid/path/bogus.file"};

    public static final String[] TEST_ARGS_INVALID_MISSING_VALUE_FOR_SCHEDULE_FILE =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + "="};

    public static final String[] TEST_ARGS_INVALID_MISSING_VALUE_FOR_HOURS_FILE =
            {"--" + HOURS_FILE.toString() + "=",
                    "--" + MOVIES_FILE.toString() + "=" + PATH_TO_JSON_TEST_MOVIE_FILE,
                    "--" + SCHEDULE_FILE.toString() + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};

    public static final String[] TEST_ARGS_INVALID_MISSING_VALUE_FOR_MOVIES_FILE =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
                    "--" + MOVIES_FILE.toString() + "=",
                    "--" + SCHEDULE_FILE.toString() + PATH_TO_TEST_SCHEDULE_OUTPUT_FILE};
}
