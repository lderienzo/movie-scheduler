package com.tandem.showtime.moviescheduler.schedule;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;


import com.tandem.showtime.moviescheduler.schedule.ScheduleUtils;

public class ScheduleUtilsTest {

    @Test
    public void testFormatTimeForSchedule_withMinutesEndingIn5() {
        // given
        LocalTime latestTimeLastShowingCanStart = new LocalTime(1, 49);

        // when
        LocalTime properlyFormattedScheduleTime = ScheduleUtils.formatTimeForSchedule(latestTimeLastShowingCanStart);

        // then
        LocalTime easyToReadScheduleTime = new LocalTime(1, 45);
        assertThat(properlyFormattedScheduleTime).isEqualTo(easyToReadScheduleTime);
    }

    @Test
    public void testFormatTimeForSchedule_withMinutesEndingIn0() {
        // given
        LocalTime latestTimeLastShowingCanStart = new LocalTime(1, 41);

        // when
        LocalTime properlyFormattedScheduleTime = ScheduleUtils.formatTimeForSchedule(latestTimeLastShowingCanStart);

        // then
        LocalTime easyToReadScheduleTime = new LocalTime(1, 40);
        assertThat(properlyFormattedScheduleTime).isEqualTo(easyToReadScheduleTime);
    }
}
