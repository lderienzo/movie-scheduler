package com.tandem.showtime.moviescheduler.movie;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class MovieTest {
    private Movie movie;

    @Test
    public void testConvertingLengthFromStringToLocalTime() {
        // given
        movie = new Movie("The Matrix (1999)", "Rated R.", "136 minutes");

        // when
        int movieLength = movie.length();

        // then
        assertThat(movieLength).isEqualTo(136);
    }

    @Test
    public void testTitleWithRatingForSchedule_Rrating() {
        // given
        movie = new Movie("The Hateful Eight (2015).", "Rated R.", "187 minutes");

        // when
        String titleWithRatingForSchedule = movie.titleWithRatingForSchedule();

        // then
        assertThat(titleWithRatingForSchedule).isEqualTo("The Hateful Eight (R)");
    }

    @Test
    public void testTitleWithRatingForSchedule_PG_13rating() {
        // given
        movie = new Movie("The Matrix (1999)", "Rated PG-13.", "136 minutes");

        // when
        String titleWithRatingForSchedule = movie.titleWithRatingForSchedule();

        // then
        assertThat(titleWithRatingForSchedule).isEqualTo("The Matrix (PG-13)");
    }

}
