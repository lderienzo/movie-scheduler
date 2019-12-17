package com.tandem.showtime.moviescheduler.arguments;

import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;


public class ApplicationDataLoader {

    private HoursDataLoader hoursDataLoader;
    private MoviesDataLoader moviesDataLoader;
    private FilePathDataLoader filePathDataLoader;


    public static final ApplicationData loadDataForArgs(ApplicationArguments args) {
        return new ApplicationDataLoader(args).getApplicationData();
    }

    private ApplicationDataLoader(ApplicationArguments args) {
        hoursDataLoader = new HoursDataLoader(args);
        moviesDataLoader = new MoviesDataLoader(args);
        filePathDataLoader = new FilePathDataLoader(args);
    }

    final ApplicationData getApplicationData() {
        Hours hours = hoursDataLoader.extractData();
        Movies movies = moviesDataLoader.extractData();
        String outFilePath = filePathDataLoader.extractData();
        return new ApplicationData(hours, movies, outFilePath);
    }
}
