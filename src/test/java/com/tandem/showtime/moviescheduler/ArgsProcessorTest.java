package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.ArgOption.*;
import static com.tandem.showtime.moviescheduler.TestConstants.*;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;


import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;

@RunWith(MockitoJUnitRunner.class)
public class ArgsProcessorTest {

    private ArgsProcessor argsProcessor;
    private ApplicationArguments givenMockArgs = mock(ApplicationArguments.class);


    @Test
    public void testGetHours() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        String hoursOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(hoursOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        Hours hours = argsProcessor.getHours();
        assertThat(hours).isNotNull();
        assertThat(hours.weekday()).isNotNull();
        assertThat(hours.weekday().opening()).isEqualTo(new LocalTime(11,0));
        assertThat(hours.weekday().closing()).isEqualTo(new LocalTime(23,0));
        assertThat(hours.weekday().startTimeForAllShowings()).isEqualTo(new LocalTime(11,15));
        assertThat(hours.weekend()).isNotNull();
        assertThat(hours.weekend().opening()).isEqualTo(new LocalTime(10,30));
        assertThat(hours.weekend().closing()).isEqualTo(new LocalTime(0,0));
        assertThat(hours.weekend().startTimeForAllShowings()).isEqualTo(new LocalTime(10,45));
    }

    @Test
    public void testGetMovies() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        String moviesOptionName = MOVIE_FILE.toString();
        when(givenMockArgs.containsOption(moviesOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        Movies movies = argsProcessor.getMovies();
        assertThat(movies).isNotNull();
        assertThat(movies.playing()).isNotNull();
        assertThat(movies.playing().size()).isEqualTo(2);
        assertThat(movies.playing().get(0).length()).isEqualTo(86);
        assertThat(movies.playing().get(1).length()).isEqualTo(136);
        assertThat(movies.playing().get(0).title()).isEqualTo("Liar Liar (1997).");
        assertThat(movies.playing().get(1).title()).isEqualTo("The Matrix (1999).");
    }

    @Test
    public void testGetOutFilePath() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        String outFilePath = argsProcessor.getOutFilePath();
        assertThat(outFilePath).isNotNull();
        assertThat(outFilePath).isEqualTo(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenOptionMissing() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_MOVIE_PATH);
        String outFileOptionName = MOVIE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenOptionMissing() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_HOURS_PATH);
        String outFileOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenOptionMissing() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_OUT_FILE_PATH);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getOutFilePath();
    }



    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenWhenInvalidFilePath() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MOVIES_FILE_PATH);
        String outFileOptionName = MOVIE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenWhenInvalidFilePath() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_HOURS_FILE_PATH);
        String outFileOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetScheduleOutputFilePath_exceptionThrownWhenWhenInvalidFilePath() {

        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_SCHEDULE_OUTPUT_FILE_PATH);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        argsProcessor.getOutFilePath();
    }

}
