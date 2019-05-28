package com.tandem.showtime.moviescheduler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializerForScheduler {

    public static Hours getHours(String pathToHoursFile) throws IOException {
        return new ObjectMapper().readValue(new File(pathToHoursFile), Hours.class);
    }

    public static Movies getMovies(String pathToMovieFile) throws IOException {
        return new ObjectMapper().readValue(new File(pathToMovieFile), Movies.class);
    }
}
