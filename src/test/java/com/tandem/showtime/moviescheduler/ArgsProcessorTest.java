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


    public void commonGivenWhenSetup() {
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(SCHEDULE_FILE.toString())).thenReturn(true);
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        argsProcessor = new ArgsProcessor(givenMockArgs);
    }

    @Test
    public void testGetHours_hoursProperlyCreatedWhenAllArgsPresentAndValid() {
        commonGivenWhenSetup();

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
    public void testGetMovies_moviesProperlyCreatedWhenAllArgsPresentAndValid() {
        commonGivenWhenSetup();

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
    public void testGetOutFilePath_outFileProperlySetWhenAllArgsPresentAndValid() {
        commonGivenWhenSetup();

        // then
        String outFilePath = argsProcessor.getOutFilePath();
        assertThat(outFilePath).isNotNull();
        assertThat(outFilePath).isEqualTo(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenOptionMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_MOVIE_PATH);
        String outFileOptionName = MOVIES_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenOptionMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_HOURS_PATH);
        String outFileOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenOptionMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_MISSING_OUT_FILE_PATH);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenInvalidFilePath() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MOVIES_FILE_PATH);
        String outFileOptionName = MOVIES_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenInvalidFilePath() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_HOURS_FILE_PATH);
        String outFileOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenInvalidFilePath() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_SCHEDULE_OUTPUT_FILE_PATH);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenValueIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_SCHEDULE_FILE);
        String outFileOptionName = SCHEDULE_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenValueIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_HOURS_FILE);
        String outFileOptionName = HOURS_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenValueIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_MOVIES_FILE);
        String outFileOptionName = MOVIES_FILE.toString();
        when(givenMockArgs.containsOption(outFileOptionName)).thenReturn(true);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }
}
