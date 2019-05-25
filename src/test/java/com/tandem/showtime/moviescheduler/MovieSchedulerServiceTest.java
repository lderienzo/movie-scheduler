package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;
import static com.tandem.showtime.moviescheduler.TestConstants.ARGS;
import static com.tandem.showtime.moviescheduler.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(MockitoJUnitRunner.class)
public class MovieSchedulerServiceTest {

//    @Autowired
//    private MovieSchedulerService movieSchedulerService;
    private ArgsProcessor argsProcessor;
    private ApplicationArguments args;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Ignore
    @Test
    public void testMovieShowingSchedulerWasInjected() {
//        assertThat(movieSchedulerService).isNotNull();
    }

    @Test
    public void testDetermineMovieScheduleForWeekdayShows() {
        //Your system should be able closing take in the details of
        // each movie and output a start and end time of each
        // showing that abides by all of the provided rules.

        // read movie details
        // -- read in movie name, Rating, and duration
        // -- output schedule for showing times For Weekdays and Weekends

        // method: generateScheduleForMovie();

        // implementation
        // assertEquals("1:10 PM - 2:36 PM 3:15 PM - 4:41 PM 5:20 PM - 6:46 PM 7:25 PM - 8:51 PM 9:30 PM - 10:56 PM",
        //          new MovieScheduler.generateShowtimesForMovie("Liar Liar (1997). Rated PG-13. 86 minutes"))

        // given
        args = mock(ApplicationArguments.class);

        // when
        when(args.getSourceArgs()).thenReturn(ARGS);    // TEST_ARGS
        // -- for Hours
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);

        // -- for Movies
        String moviesOptionName = MOVIE_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);

        argsProcessor = new ArgsProcessor(args);

        // then
        // -- get hours
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        // -- get movies
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(4);


        // then when
        MovieSchedulerService movieSchedulerService = new MovieSchedulerService(hours, movies);
        movieSchedulerService.determineMovieScheduleForWeekdayShows();

    }

}