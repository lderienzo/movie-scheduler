package com.tandem.showtime.moviescheduler.arguments;

import java.io.IOException;

import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.schedule.JsonDeserializerForScheduler;
import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;
import com.tandem.showtime.moviescheduler.movie.Movies;

public final class MoviesDataLoader extends AbstractJsonDataLoader<Movies> {

    private String moviesFilePath;


    public MoviesDataLoader(ApplicationArguments args) {
        this.args = args;
    }

    @Override
    public final Movies extractData() {
        return extractMovies();
    }

    private Movies extractMovies() {
        moviesFilePath = getMoviesFilePathFromApplicationArguments();
        return loadMoviesFromFileUsingPath();
    }

    private String getMoviesFilePathFromApplicationArguments(){
        return extractValueForArgument(ArgOption.MOVIES_FILE);
    }

    private Movies loadMoviesFromFileUsingPath() {
        Movies movies;
        try {
            movies = deserializeJsonFileToObject(moviesFilePath);
        } catch(IOException e) {
            throw new ArgsProcessorException(e.getMessage());
        }
        return movies;
    }

    @Override
    protected final Movies deserializeJsonFileToObject(String moviesFilePath) throws IOException {
        return JsonDeserializerForScheduler.getMovies(moviesFilePath);    // TODO: refactor name to SchedulerJsonDeserializer
    }
}
