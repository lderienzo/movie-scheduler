package com.tandem.showtime.moviescheduler;

import java.io.File;

public class TestFileUtils {

     public String getAbsolutePathOfResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
