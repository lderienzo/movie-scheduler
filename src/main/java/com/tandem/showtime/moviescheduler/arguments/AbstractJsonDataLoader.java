package com.tandem.showtime.moviescheduler.arguments;

import java.io.IOException;

public abstract class AbstractJsonDataLoader<T> extends AbstractDataLoader {
    protected abstract <T> T deserializeJsonFileToObject(String hoursFilePath) throws IOException;
}
