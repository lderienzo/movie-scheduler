package com.tandem.showtime.moviescheduler.schedule;

import static com.tandem.showtime.moviescheduler.arguments.ArgOption.*;
import static com.tandem.showtime.moviescheduler.utils.TestConstants.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.springframework.boot.ApplicationArguments;

import com.tandem.showtime.moviescheduler.ScheduleGenerationTestUtils;
import com.tandem.showtime.moviescheduler.arguments.*;


public class SchedulePdfWriterTest extends ScheduleGenerationTestUtils {

    @Test
    public void testWriteSchedule() {
        // given
        ApplicationArguments args = mock(ApplicationArguments.class);
        when(args.getSourceArgs()).thenReturn(TEST_ARGS);
        when(args.containsOption(HOURS_FILE.toString())).thenReturn(true);
        when(args.containsOption(MOVIES_FILE.toString())).thenReturn(true);
        when(args.containsOption(SCHEDULE_FILE.toString())).thenReturn(true);
        // when
        ApplicationData appData = ApplicationDataLoader.loadDataForArgs(args);
        MovieScheduleGenerator movieScheduleGenerator = new MovieScheduleGenerator(appData.getHours(),  appData.getMovies());
        movieScheduleGenerator.generateSchedules();
        Schedule weekdaySchedule = movieScheduleGenerator.getWeekdaySchedule();
        Schedule weekendSchedule = movieScheduleGenerator.getWeekendSchedule();
        SchedulePdfWriter schedulePdfWriter = new SchedulePdfWriter(weekdaySchedule, weekendSchedule);
        schedulePdfWriter.writeSchedules(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
        // then
        super.checkThatScheduleWasGenerated();
    }
}
