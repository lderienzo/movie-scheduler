package com.tandem.showtime.moviescheduler.schedule;

import static com.tandem.showtime.moviescheduler.utils.TestConstants.PATH_TO_JSON_MOVIE_FILE;
import static org.assertj.core.api.Java6Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.tandem.showtime.moviescheduler.movie.Movie;
import com.tandem.showtime.moviescheduler.movie.Movies;

public class JsonDeserializerForSchedulerTest {

    @Test
    public void testGetMovies() throws IOException {
        // given/when
        Movies deserializedMovies = JsonDeserializerForScheduler.getMovies(PATH_TO_JSON_MOVIE_FILE);

        // then
        assertThat(deserializedMovies).isNotNull();
        List<Movie> movieList = deserializedMovies.playing();
        assertThat(movieList.toArray()).hasSize(4);
    }
}
