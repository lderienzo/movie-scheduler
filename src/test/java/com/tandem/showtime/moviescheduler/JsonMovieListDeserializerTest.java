package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.TestConstants.PATH_TO_JSON_MOVIE_LIST_FILE;
import static org.assertj.core.api.Java6Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class JsonMovieListDeserializerTest {

    @Test
    public void testGetMovies() throws IOException {
        // given/when
        Movies deserializedMovies = JsonMovieListDeserializer.getMovies(PATH_TO_JSON_MOVIE_LIST_FILE);

        // then
        assertThat(deserializedMovies).isNotNull();
        List<Movie> movieList = deserializedMovies.get();
        assertThat(movieList.toArray()).hasSize(4);
    }
}
