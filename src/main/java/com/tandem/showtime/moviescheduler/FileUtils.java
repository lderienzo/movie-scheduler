package com.tandem.showtime.moviescheduler;

import java.io.File;

public class FileUtils {

    // TODO: check if this is only used in testing, if so move to test package and rename to "TestFileUtils"
    public String getAbsolutePathOfResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
