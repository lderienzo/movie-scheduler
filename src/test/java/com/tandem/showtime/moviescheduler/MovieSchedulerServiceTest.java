package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIE_FILE;
import static com.tandem.showtime.moviescheduler.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.LocalTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;


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
        // given
        args = mock(ApplicationArguments.class);

        // when
        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        // -- for Hours
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);

        // -- for Movies
        String moviesOptionName = MOVIE_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);

        argsProcessor = new ArgsProcessor(args);

        // then
        // -- moviesPlaying hours
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        // -- moviesPlaying movies
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);


        // then when
        MovieSchedulerService movieSchedulerService = new MovieSchedulerService(hours, movies);
        Schedule schedule = movieSchedulerService.determineMovieScheduleForWeekdayShowings();
        assertThat(schedule).isNotNull();
        assertThat(schedule.moviesPlaying()).hasSize(2);
        assertThat(schedule.moviesPlaying().get(0).title()).isEqualTo("Liar Liar (1997).");
        assertThat(startTimeForFirtMoviePlaying(0, schedule)).isEqualTo(new LocalTime(21, 30)); //9:30
        assertThat(endTimeForFirtMoviePlaying(0, schedule)).isEqualTo(new LocalTime(22, 56));
        assertThat(startTimeForFirtMoviePlaying(1, schedule)).isEqualTo(new LocalTime(19, 25));
        assertThat(endTimeForFirtMoviePlaying(1, schedule)).isEqualTo(new LocalTime(20, 51));
        assertThat(startTimeForFirtMoviePlaying(2, schedule)).isEqualTo(new LocalTime(17, 20));
        assertThat(endTimeForFirtMoviePlaying(2, schedule)).isEqualTo(new LocalTime(18, 46));
        assertThat(startTimeForFirtMoviePlaying(3, schedule)).isEqualTo(new LocalTime(15, 15));
        assertThat(endTimeForFirtMoviePlaying(3, schedule)).isEqualTo(new LocalTime(16, 41));
        assertThat(startTimeForFirtMoviePlaying(4, schedule)).isEqualTo(new LocalTime(13, 10));
        assertThat(endTimeForFirtMoviePlaying(4, schedule)).isEqualTo(new LocalTime(14, 36));
    }

    private LocalTime startTimeForFirtMoviePlaying(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).getShowings().get(showingNumber).startTime();
    }

    private LocalTime endTimeForFirtMoviePlaying(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).getShowings().get(showingNumber).endTime();
    }

    @Test
    public void testDetermineMovieScheduleForWeekendShows() {
        // given
        args = mock(ApplicationArguments.class);

        // when
        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        // -- for Hours
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);

        // -- for Movies
        String moviesOptionName = MOVIE_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);

        argsProcessor = new ArgsProcessor(args);

        // then
        // -- moviesPlaying hours
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        // -- moviesPlaying movies
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);


        // then when
        MovieSchedulerService movieSchedulerService = new MovieSchedulerService(hours, movies);
        Schedule schedule = movieSchedulerService.determineMovieScheduleForWeekendShowings();
        assertThat(schedule).isNotNull();
        assertThat(schedule.moviesPlaying()).hasSize(2);
        assertThat(schedule.moviesPlaying().get(0).title()).isEqualTo("Liar Liar (1997).");
        assertThat(startTimeForFirtMoviePlaying(0, schedule)).isEqualTo(new LocalTime(22, 30));
        assertThat(endTimeForFirtMoviePlaying(0, schedule)).isEqualTo(new LocalTime(23, 56));
        assertThat(startTimeForFirtMoviePlaying(1, schedule)).isEqualTo(new LocalTime(20, 25));
        assertThat(endTimeForFirtMoviePlaying(1, schedule)).isEqualTo(new LocalTime(21, 51));
        assertThat(startTimeForFirtMoviePlaying(2, schedule)).isEqualTo(new LocalTime(18, 20));
        assertThat(endTimeForFirtMoviePlaying(2, schedule)).isEqualTo(new LocalTime(19, 46));
        assertThat(startTimeForFirtMoviePlaying(3, schedule)).isEqualTo(new LocalTime(16, 15));
        assertThat(endTimeForFirtMoviePlaying(3, schedule)).isEqualTo(new LocalTime(17, 41));
        assertThat(startTimeForFirtMoviePlaying(4, schedule)).isEqualTo(new LocalTime(14, 10));
        assertThat(endTimeForFirtMoviePlaying(4, schedule)).isEqualTo(new LocalTime(15, 36));
        assertThat(startTimeForFirtMoviePlaying(5, schedule)).isEqualTo(new LocalTime(12, 5));
        assertThat(endTimeForFirtMoviePlaying(5, schedule)).isEqualTo(new LocalTime(13, 31));
    }
}