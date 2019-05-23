package com.tandem.showtime.moviescheduler;

import java.io.File;

public class Utils {
    public static final String MOVIES_JSON_FILE_NAME = "movies.json";

    public String getAbsolutePathOfMoviesJsonResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
