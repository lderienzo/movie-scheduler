package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.HOURS_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.MOVIES_FILE;
import static com.tandem.showtime.moviescheduler.ArgOption.SCHEDULE_FILE;
import static com.tandem.showtime.moviescheduler.TestConstants.PATH_TO_TEST_SCHEDULE_OUTPUT_FILE;
import static com.tandem.showtime.moviescheduler.TestConstants.TEST_ARGS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.springframework.boot.ApplicationArguments;

import com.itextpdf.text.DocumentException;

public class SchedulePdfWriterServiceTest {

    private ArgsProcessor argsProcessor;
    private ApplicationArguments args;

    @Test
    public void testWriteSchedule() throws FileNotFoundException, DocumentException {
        // given
        args = mock(ApplicationArguments.class);

        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        String hoursOptionName = HOURS_FILE.toString();
        when(args.containsOption(hoursOptionName)).thenReturn(true);
        String moviesOptionName = MOVIES_FILE.toString();
        when(args.containsOption(moviesOptionName)).thenReturn(true);
        String scheduleOutputFileOptionName = SCHEDULE_FILE.toString();
        when(args.containsOption(scheduleOutputFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(args);


        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekend()).isNotNull();
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).hasSize(2);


        MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(hours, movies, PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
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
        SchedulePdfWriterService schedulePdfWriterService = new SchedulePdfWriterService(weekdaySchedule, weekendSchedule);
        schedulePdfWriterService.writeSchedules(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
    }
}
