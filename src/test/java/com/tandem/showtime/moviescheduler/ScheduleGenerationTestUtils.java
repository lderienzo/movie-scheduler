package com.tandem.showtime.moviescheduler;

import static com.tandem.showtime.moviescheduler.utils.TestConstants.PATH_TO_TEST_SCHEDULE_OUTPUT_FILE;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;

public class ScheduleGenerationTestUtils {

    @AfterEach
    public final void deleteGeneratedSchedule() throws IOException {
        Path filePath = Paths.get(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
        Files.delete(filePath);
    }


    public final void checkThatScheduleWasGenerated() {
        File tempFile = new File(PATH_TO_TEST_SCHEDULE_OUTPUT_FILE);
        assertThat(tempFile.exists()).isTrue();
    }

}
