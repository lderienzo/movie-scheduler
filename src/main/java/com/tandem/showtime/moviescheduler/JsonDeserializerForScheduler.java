package com.tandem.showtime.moviescheduler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializerForScheduler {

    public static Hours getHours(String pathToHoursFile) throws IOException {
        Hours hours = new ObjectMapper().readValue(new File(pathToHoursFile), Hours.class);
        return hours;
    }

    public static Movies getMovies(String pathToMovieFile) throws IOException {
        Movies movies = new ObjectMapper().readValue(new File(pathToMovieFile), Movies.class);
        return movies;
    }
}
