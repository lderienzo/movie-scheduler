package com.tandem.showtime.moviescheduler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMovieListDeserializer {
    private static Movies movies;  // TODO can this be immutable somehow?

    public static Movies getMovies(String pathToJsonMovieListFile) throws IOException {    // TODO: make sure IOException is RuntimeException
        movies = new ObjectMapper().readValue(new File(pathToJsonMovieListFile), Movies.class);
        return movies;
    }
}
