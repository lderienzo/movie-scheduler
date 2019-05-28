package com.tandem.showtime.moviescheduler.arguments;

import static com.tandem.showtime.moviescheduler.arguments.ArgOption.*;
import static com.tandem.showtime.moviescheduler.utils.TestConstants.*;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;


import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;

@RunWith(MockitoJUnitRunner.class)
public class ArgsProcessorTest {

    private ArgsProcessor argsProcessor;
    private final ApplicationArguments givenMockArgs = mock(ApplicationArguments.class);


    public void commonGivenWhenSetup() {
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(SCHEDULE_FILE.toString())).thenReturn(true);
    }

    @Test
    public void testGetHours_hoursProperlyCreatedWhenAllArgsPresentAndValid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        commonGivenWhenSetup();
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
    public void testGetMovies_moviesProperlyCreatedWhenAllArgsPresentAndValid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        commonGivenWhenSetup();
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
    public void testGetOutFilePath_outFileProperlySetWhenAllArgsPresentAndValid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        String outFilePath = argsProcessor.getOutFilePath();
        assertThat(outFilePath).isNotNull();
        assertThat(outFilePath).isEqualTo(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
    }

    @Test
    public void testGetOutFilePath_outFileProperlySetWhenJustFileName() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_WITH_JUST_OUT_FILE_NAME);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        // then
        String outFilePath = argsProcessor.getOutFilePath();
        assertThat(outFilePath).isNotNull();
        assertThat(outFilePath).isEqualTo(SCHEDULE_OUT_FILE_NAME);
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenMovieFileOptionMissing() {
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(false);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenHoursFileOptionMissing() {
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(false);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenScheduleOutPutFileOptionMissing() {
        when(givenMockArgs.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(givenMockArgs.containsOption(SCHEDULE_FILE.toString())).thenReturn(false);
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenMoviesFilePathIsInvalid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MOVIES_FILE_PATH);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenHoursFilePathIsInvalid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_HOURS_FILE_PATH);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenOutputFilePathIsInvalid() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_SCHEDULE_OUTPUT_FILE_PATH);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetOutFilePath_exceptionThrownWhenValueForOutFilePathIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_SCHEDULE_FILE);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getOutFilePath();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetHours_exceptionThrownWhenValueForHoursFilePathIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_HOURS_FILE);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getHours();
    }

    @Test(expected = ArgsProcessorException.class)
    public void testGetMovies_exceptionThrownWhenValueForMoviesFilePathIsMissing() {
        when(givenMockArgs.getSourceArgs()).thenReturn(TEST_ARGS_INVALID_MISSING_VALUE_FOR_MOVIES_FILE);
        commonGivenWhenSetup();
        argsProcessor = new ArgsProcessor(givenMockArgs);

        argsProcessor.getMovies();
    }
}