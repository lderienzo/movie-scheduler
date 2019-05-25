package com.tandem.showtime.moviescheduler;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

public class MovieTest {
    private Movie movie;

    @Test
    public void testConvertingLengthFromStringToLocalTime() {
        // given
        movie = new Movie("The Matrix", "Rated R.", "136 minutes");

        // when
        int movieLength = movie.length();

        // then
        assertThat(movieLength).isEqualTo(136);
    }

    class MovieRunningTime {

    }
}
