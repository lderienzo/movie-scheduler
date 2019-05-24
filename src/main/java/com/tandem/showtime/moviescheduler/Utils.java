package com.tandem.showtime.moviescheduler;

import java.io.File;

public class Utils {
    public static final String HOURS_JSON_FILE_NAME = "hours.json";
    public static final String MOVIES_JSON_FILE_NAME = "movies.json";

    public String getAbsolutePathOfJsonFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
