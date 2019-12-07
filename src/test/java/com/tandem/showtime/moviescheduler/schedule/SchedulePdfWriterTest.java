package com.tandem.showtime.moviescheduler.schedule;

import static com.tandem.showtime.moviescheduler.arguments.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.MOVIES_FILE;
import static com.tandem.showtime.moviescheduler.arguments.ArgOption.SCHEDULE_FILE;
import static com.tandem.showtime.moviescheduler.utils.TestConstants.PATH_TO_TEST_SCHEDULE_OUTPUT_FILE;
import static com.tandem.showtime.moviescheduler.utils.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.arguments.ArgsProcessor;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;


public class SchedulePdfWriterTest {

    @Test
    public void testWriteSchedule() {
        // given
        ApplicationArguments args = mock(ApplicationArguments.class);

        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);
        String moviesOptionName = MOVIES_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);
        String scheduleOutputFileOptionName = SCHEDULE_FILE.toString();
        when(args.containsOption(scheduleOutputFileOptionName)).thenReturn(true);
        ArgsProcessor argsProcessor = new ArgsProcessor(args);


        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);


        MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(hours, movies);
        movieScheduleGenerator.generateSchedules();

        // weekday schedule
        Schedule weekdaySchedule = movieScheduleGenerator.getWeekdaySchedule();
        assertThat(weekdaySchedule).isNotNull();
        assertThat(weekdaySchedule.moviesPlaying()).hasSize(2);
        assertThat(weekdaySchedule.moviesPlaying().get(0).weekdayShowings()).hasSize(5);
        assertThat(weekdaySchedule.moviesPlaying().get(1).weekdayShowings()).hasSize(4);

        // weekend schedule
        Schedule weekendSchedule = movieScheduleGenerator.getWeekendSchedule();
        assertThat(weekendSchedule).isNotNull();
        assertThat(weekendSchedule.moviesPlaying()).hasSize(2);
        assertThat(weekdaySchedule.moviesPlaying().get(0).weekendShowings()).hasSize(6);
        assertThat(weekdaySchedule.moviesPlaying().get(1).weekendShowings()).hasSize(4);

        // when
        SchedulePdfWriter schedulePdfWriter = new SchedulePdfWriter(weekdaySchedule, weekendSchedule);
        schedulePdfWriter.writeSchedules(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);

        // TODO: As part of test verification, read schedule from filesystem to ensure it was at least created. Not sure how to test formatting.
    }
}
