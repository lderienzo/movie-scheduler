package com.tandem.showtime.moviescheduler.arguments;

import java.io.IOException;

import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.schedule.JsonDeserializerForScheduler;
import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;
import com.tandem.showtime.moviescheduler.hours.Hours;

public final class HoursDataLoader extends AbstractJsonDataLoader<Hours> {

    private String hoursFilePath;


    public HoursDataLoader(ApplicationArguments args) {
        this.args = args;
    }

    @Override
    public final Hours extractData() {
        return extractHours();
    }

    private Hours extractHours() {
        hoursFilePath = getHoursFilePathFromApplicationArguments();
        return loadHoursFromFileUsingPath();
    }

    private String getHoursFilePathFromApplicationArguments(){
        return extractValueForArgument(ArgOption.HOURS_FILE);
    }

    private Hours loadHoursFromFileUsingPath() {
        Hours hours;
        try {
            hours = deserializeJsonFileToObject(hoursFilePath);
        } catch(IOException e) {
            throw new ArgsProcessorException(e.getMessage());
        }
        return hours;
    }

    @Override
    protected final Hours deserializeJsonFileToObject(String hoursFilePath) throws IOException {
        return JsonDeserializerForScheduler.getHours(hoursFilePath);    // TODO: refactor name to SchedulerJsonDeserializer
    }
}
