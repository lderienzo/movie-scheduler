package com.tandem.showtime.moviescheduler;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class TestJsonMovieListDeserializer {
    private static final String MOVIES_JSON_FILE = "movies.json";


    @Test
    public void testGetMovies() throws IOException {
        // given
        String pathOfMoviesJsonResourceFile = getAbsolutePathOfMoviesJsonResourceFile();

        // when
        Movies deserializedMovies = JsonMovieListDeserializer.getMovies(pathOfMoviesJsonResourceFile);

        // then
        assertThat(deserializedMovies, is(notNullValue()));
        List<Movie> movieList = deserializedMovies.get();
        assertThat(movieList.toArray(), is(arrayWithSize(4)));
    }

    private String getAbsolutePathOfMoviesJsonResourceFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(MOVIES_JSON_FILE).getFile());
        return file.getAbsolutePath();
    }
}
