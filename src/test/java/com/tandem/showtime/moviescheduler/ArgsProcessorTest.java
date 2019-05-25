package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;
import static com.tandem.showtime.moviescheduler.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;

@RunWith(MockitoJUnitRunner.class)
public class ArgsProcessorTest {

    private ArgsProcessor argsProcessor;
    private ApplicationArguments args;


    @Test
    public void testGetHours() {
        // given
        args = mock(ApplicationArguments.class);

        // when
        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(args);

        // then
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
    }

    @Test
    public void testGetMovies() {
        // given
        args = mock(ApplicationArguments.class);

        // when
        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        String moviesOptionName = MOVIE_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(args);

        // then
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).isNotNull();
        assertThat(movies.playing().size()).isEqualTo(1);
        assertThat(movies.playing().get(0).length()).isEqualTo("136 minutes");
    }

}
