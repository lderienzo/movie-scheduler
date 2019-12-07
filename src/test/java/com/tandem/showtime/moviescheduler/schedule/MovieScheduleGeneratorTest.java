package com.tandem.showtime.moviescheduler.schedule;

import static com.tandem.showtime.moviescheduler.arguments.ArgOption.*;
import static com.tandem.showtime.moviescheduler.utils.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.arguments.ArgsProcessor;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;


@ExtendWith(MockitoExtension.class)
public class MovieScheduleGeneratorTest {
    private ArgsProcessor argsProcessor;
    private ApplicationArguments givenMockArgs;

    @Test
    public void testDetermineMovieScheduleForWeekdayShows() {
        // given
        givenMockArgs = mock(ApplicationArguments.class);
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(SCHEDULE_FILE.toString())).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);

        // then -- when
        MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(hours, movies);
        movieScheduleGenerator.generateSchedules();

        Schedule schedule = movieScheduleGenerator.getWeekdaySchedule();
        assertThat(schedule).isNotNull();
        assertThat(schedule.moviesPlaying()).hasSize(2);
        assertThat(schedule.moviesPlaying().get(0).weekdayShowings()).hasSize(5);
        assertThat(schedule.moviesPlaying().get(0).title()).isEqualTo("Liar Liar (1997).");
        assertThat(startTimeForFirstWeekdayShowing(0, schedule)).isEqualTo(new LocalTime(21, 30)); //9:30
        assertThat(endTimeForFirstWeekdayShowing(0, schedule)).isEqualTo(new LocalTime(22, 56));
        assertThat(startTimeForFirstWeekdayShowing(1, schedule)).isEqualTo(new LocalTime(19, 25));
        assertThat(endTimeForFirstWeekdayShowing(1, schedule)).isEqualTo(new LocalTime(20, 51));
        assertThat(startTimeForFirstWeekdayShowing(2, schedule)).isEqualTo(new LocalTime(17, 20));
        assertThat(endTimeForFirstWeekdayShowing(2, schedule)).isEqualTo(new LocalTime(18, 46));
        assertThat(startTimeForFirstWeekdayShowing(3, schedule)).isEqualTo(new LocalTime(15, 15));
        assertThat(endTimeForFirstWeekdayShowing(3, schedule)).isEqualTo(new LocalTime(16, 41));
        assertThat(startTimeForFirstWeekdayShowing(4, schedule)).isEqualTo(new LocalTime(13, 10));
        assertThat(endTimeForFirstWeekdayShowing(4, schedule)).isEqualTo(new LocalTime(14, 36));
    }

    private LocalTime startTimeForFirstWeekdayShowing(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).weekdayShowings().get(showingNumber).startTime();
    }

    private LocalTime endTimeForFirstWeekdayShowing(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).weekdayShowings().get(showingNumber).endTime();
    }

    @Test
    public void testDetermineMovieScheduleForWeekendShows() {
        // given
        givenMockArgs = mock(ApplicationArguments.class);
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(SCHEDULE_FILE.toString())).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);

        // then when
        MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(hours, movies);
        movieScheduleGenerator.generateSchedules();

        Schedule schedule = movieScheduleGenerator.getWeekendSchedule();
        assertThat(schedule).isNotNull();
        assertThat(schedule.moviesPlaying()).hasSize(2);
        assertThat(schedule.moviesPlaying().get(0).weekendShowings()).hasSize(6);
        assertThat(schedule.moviesPlaying().get(0).title()).isEqualTo("Liar Liar (1997).");
        assertThat(startTimeForFirstWeekendShowing(0, schedule)).isEqualTo(new LocalTime(22, 30));
        assertThat(endTimeForFirstWeekendShowing(0, schedule)).isEqualTo(new LocalTime(23, 56));
        assertThat(startTimeForFirstWeekendShowing(1, schedule)).isEqualTo(new LocalTime(20, 25));
        assertThat(endTimeForFirstWeekendShowing(1, schedule)).isEqualTo(new LocalTime(21, 51));
        assertThat(startTimeForFirstWeekendShowing(2, schedule)).isEqualTo(new LocalTime(18, 20));
        assertThat(endTimeForFirstWeekendShowing(2, schedule)).isEqualTo(new LocalTime(19, 46));
        assertThat(startTimeForFirstWeekendShowing(3, schedule)).isEqualTo(new LocalTime(16, 15));
        assertThat(endTimeForFirstWeekendShowing(3, schedule)).isEqualTo(new LocalTime(17, 41));
        assertThat(startTimeForFirstWeekendShowing(4, schedule)).isEqualTo(new LocalTime(14, 10));
        assertThat(endTimeForFirstWeekendShowing(4, schedule)).isEqualTo(new LocalTime(15, 36));
        assertThat(startTimeForFirstWeekendShowing(5, schedule)).isEqualTo(new LocalTime(12, 5));
        assertThat(endTimeForFirstWeekendShowing(5, schedule)).isEqualTo(new LocalTime(13, 31));
    }

    private LocalTime startTimeForFirstWeekendShowing(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).weekendShowings().get(showingNumber).startTime();
    }

    private LocalTime endTimeForFirstWeekendShowing(int showingNumber, Schedule schedule) {
        return schedule.moviesPlaying().get(0).weekendShowings().get(showingNumber).endTime();
    }
}