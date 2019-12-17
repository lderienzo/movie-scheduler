package com.tandem.showtime.moviescheduler.arguments;

import org.springframework.boot.ApplicationArguments;

import com.google.common.base.Strings;
import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;

public final class FilePathDataLoader extends AbstractDataLoader<String> {

    private String extractedPath;


    public FilePathDataLoader(ApplicationArguments args) {
        this.args = args;
    }

    @Override
    public final String extractData() {
        return extractFilePath();
    }

    private String extractFilePath() {
        extractedPath = extractValueForArgument(ArgOption.SCHEDULE_FILE);
        if (pathIsEmpty())
            throw new ArgsProcessorException("Invalid out-file path.");
        return extractedPath;
    }

    private boolean pathIsEmpty() {
        return Strings.isNullOrEmpty(extractedPath);
    }
}
