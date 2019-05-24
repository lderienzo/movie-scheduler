package com.tandem.showtime.moviescheduler;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieSchedulerServiceTest {

    @Autowired
    private MovieSchedulerService movieSchedulerService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Ignore
    @Test
    public void testMovieShowingSchedulerWasInjected() {
        assertThat(movieSchedulerService).isNotNull();
    }

    @Ignore
    @Test
    public void testRun() {
        //Your system should be able to take in the details of
        // each movie and output a start and end time of each
        // showing that abides by all of the provided rules.

        // read movie details
        // -- read in movie name, Rating, and duration
        // -- output schedule for showing times For Weekdays and Weekends

        // method: generateScheduleForMovie();

        // implementation
        // assertEquals("1:10 PM - 2:36 PM 3:15 PM - 4:41 PM 5:20 PM - 6:46 PM 7:25 PM - 8:51 PM 9:30 PM - 10:56 PM",
        //          new MovieScheduler.generateShowtimesForMovie("Liar Liar (1997). Rated PG-13. 86 minutes"))

    }

}