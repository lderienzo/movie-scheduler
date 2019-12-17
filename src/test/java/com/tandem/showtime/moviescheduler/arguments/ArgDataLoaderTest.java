package com.tandem.showtime.moviescheduler.arguments;

import static com.tandem.showtime.moviescheduler.utils.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.exceptions.ArgsProcessorException;
import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movie;
import com.tandem.showtime.moviescheduler.movie.Movies;

public class ArgDataLoaderTest {

    private final ApplicationArguments mockArgs = mock(ApplicationArguments.class);
    private HoursDataLoader hoursDataLoader;
    private MoviesDataLoader moviesDataLoader;
    private FilePathDataLoader filePathDataLoader;


    @Nested
    class testHoursArgDataLoader {

        @Test
        public void whenHoursFilePathArgValueValidThenHoursDataLoaded() {
            // given
            hoursDataLoader = setHoursDataLoaderWithArgs(TEST_ARGS);
            // when
            Hours hours = hoursDataLoader.extractData();
            // then
            assertThatHoursReturnedEqualExpected(hours);
        }

        private HoursDataLoader setHoursDataLoaderWithArgs(String[] args) {
            when(mockArgs.getSourceArgs()).thenReturn(args);
            return new HoursDataLoader(mockArgs);
        }

        private void assertThatHoursReturnedEqualExpected(Hours hours) {
            assertThatWeekdayHoursEqualExpected(hours);
            assertThatWeekendHoursEqualExpected(hours);
        }

        private void assertThatWeekdayHoursEqualExpected(Hours hours) {
            assertThat(hours.weekday().opening().getHourOfDay()).isEqualTo(11);
            assertThat(hours.weekday().closing().getHourOfDay()).isEqualTo(23);
        }

        private void assertThatWeekendHoursEqualExpected(Hours hours) {
            assertThat(hours.weekend().opening().getHourOfDay()).isEqualTo(10);
            assertThat(hours.weekend().opening().getMinuteOfHour()).isEqualTo(30);
            assertThat(hours.weekend().closing().getHourOfDay()).isEqualTo(0);
        }

        @Test
        public void whenHoursFilePathArgValueInValidThenArgsProcessorExceptionThrown() {
            // given
            hoursDataLoader = setHoursDataLoaderWithArgs(TEST_ARGS_INVALID_HOURS_FILE_PATH);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                hoursDataLoader.extractData();
            });
        }

        @Test
        public void whenHoursArgValueMissingThenArgsProcessorExceptionThrown() {
            // given
            hoursDataLoader = setHoursDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_VALUE_FOR_HOURS_FILE);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                hoursDataLoader.extractData();
            });
        }

        @Test
        public void whenEntireHoursArgIsMissingThenArgsProcessorExceptionThrown() {
            // given
            hoursDataLoader = setHoursDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_HOURS_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                hoursDataLoader.extractData();
            });
        }

        @Test
        public void whenDuplicateHoursArgThenArgsProcessorExceptionThrown() {
            // given
            hoursDataLoader = setHoursDataLoaderWithArgs(TEST_ARGS_INVALID_ADDITIONAL_HOURS_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                hoursDataLoader.extractData();
            });
        }
    }


    @Nested
    class testMoviesArgDataLoader {

        @Test
        public void whenMoviesArgValueValidThenMoviesDataLoaded() {
            // given
            when(mockArgs.getSourceArgs()).thenReturn(TEST_ARGS);
            moviesDataLoader = new MoviesDataLoader(mockArgs);
            // when
            Movies movies = moviesDataLoader.extractData();
            // then
            assertThat(movies.playing()).containsExactlyInAnyOrder(
                    new Movie("The Matrix (1999).","Rated R.","136 minutes"),
                    new Movie("Liar Liar (1997).","Rated PG-13.","86 minutes"));
        }

        @Test
        public void whenMoviesFilePathArgValueInvalidThenArgsProcessorExceptionThrown() {
            // given
            moviesDataLoader = setMoviesDataLoaderWithArgs(TEST_ARGS_INVALID_MOVIES_FILE_PATH);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                moviesDataLoader.extractData();
            });
        }

        private MoviesDataLoader setMoviesDataLoaderWithArgs(String[] args) {
            when(mockArgs.getSourceArgs()).thenReturn(args);
            return new MoviesDataLoader(mockArgs);
        }

        @Test
        public void whenMoviesFilePathArgValueMissingThenArgsProcessorExceptionThrown() {
            // given
            moviesDataLoader = setMoviesDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_VALUE_FOR_MOVIES_FILE);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                moviesDataLoader.extractData();
            });
        }

        @Test
        public void whenEntireMoviesArgIsMissingThenArgsProcessorExceptionThrown() {
            // given
            moviesDataLoader = setMoviesDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_MOVIES_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                moviesDataLoader.extractData();
            });
        }

        @Test
        public void whenDuplicateMoviesArgThenArgsProcessorExceptionThrown() {
            // given
            moviesDataLoader = setMoviesDataLoaderWithArgs(TEST_ARGS_INVALID_ADDITIONAL_MOVIES_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                moviesDataLoader.extractData();
            });
        }

    }


    @Nested
    class testFilePathDataLoader {

        @Test
        public void whenOutFilePathArgValueValidThenValueSet() {
            // given
            filePathDataLoader = setFilePathDataLoaderWithArgs(TEST_ARGS);
            // when
            String outFilePath = filePathDataLoader.extractData();
            // then
            assertThat(outFilePath).isEqualTo("/Users/lderienzo/Downloads/movie_schedule.pdf");
        }

        private FilePathDataLoader setFilePathDataLoaderWithArgs(String[] args) {
            when(mockArgs.getSourceArgs()).thenReturn(args);
            return new FilePathDataLoader(mockArgs);
        }

        @Test
        public void whenOutFilePathArgValueMissingThenArgsProcessorExceptionThrown() {
            // given
            filePathDataLoader = setFilePathDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_VALUE_FOR_SCHEDULE_FILE);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                filePathDataLoader.extractData();
            });
        }

        @Test
        public void whenEntireOutFilePathArgIsMissingThenArgsProcessorExceptionThrown() {
            // given
            filePathDataLoader = setFilePathDataLoaderWithArgs(TEST_ARGS_INVALID_MISSING_SCHEDULE_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                filePathDataLoader.extractData();
            });
        }

        @Test
        public void whenDuplicateOutFilePathArgThenArgsProcessorExceptionThrown() {
            // given
            filePathDataLoader = setFilePathDataLoaderWithArgs(TEST_ARGS_INVALID_ADDITIONAL_SCHEDULE_FILE_ARG);
            // when/then
            Assertions.assertThrows(ArgsProcessorException.class, () -> {
                filePathDataLoader.extractData();
            });
        }
    }
}
