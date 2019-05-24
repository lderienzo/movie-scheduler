package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

import org.springframework.stereotype.Component;

@Component
public class TestConstants {

    public static String PATH_TO_JSON_HOURS_FILE =
            new Utils().getAbsolutePathOfJsonFile(Utils.HOURS_JSON_FILE_NAME);

    public static String PATH_TO_JSON_MOVIE_FILE =
            new Utils().getAbsolutePathOfJsonFile(Utils.MOVIES_JSON_FILE_NAME);

    public static final String[] ARGS =
            {"--" + HOURS_FILE.toString() + "=" + PATH_TO_JSON_HOURS_FILE,
            "--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_MOVIE_FILE};
}
