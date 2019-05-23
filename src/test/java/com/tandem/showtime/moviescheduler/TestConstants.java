package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;

public class TestConstants {
    public static String PATH_TO_JSON_MOVIE_LIST_FILE =
            new Utils().getAbsolutePathOfMoviesJsonResourceFile(Utils.MOVIES_JSON_FILE_NAME);
    public static final String[] ARGS = {"--" + MOVIE_FILE.toString() + "=" + PATH_TO_JSON_MOVIE_LIST_FILE};
}
