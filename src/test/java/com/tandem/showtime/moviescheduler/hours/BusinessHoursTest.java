package com.tandem.showtime.moviescheduler.hours;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;


public class BusinessHoursTest {
    private static final String WEEKDAY_DAYS = "Monday - Thursday";
    private static final String WEEKDAY_HOURS = "11am–11pm";
    private static final String WEEKEND_DAYS = "Friday - Sunday";
    private static final String WEEKEND_HOURS = "10:30 am–12 am";
    private BusinessHours businessHours;


    @Test
    public void testWeekdayHoursAreCorrect_withSameHourWithoutColon() {
        // given
        businessHours = new BusinessHours(WEEKDAY_DAYS, WEEKDAY_HOURS);

        // when
        LocalTime opening = businessHours.opening();
        LocalTime closing = businessHours.closing();

        // then
        assertThat(opening).isEqualTo(new LocalTime(11,0));
        assertThat(closing).isEqualTo(new LocalTime(23,0));
    }

    @Test
    public void testWeekendHoursAreCorrect_bothInAM() {
        // given
        businessHours = new BusinessHours(WEEKEND_DAYS, WEEKEND_HOURS);

        // when
        LocalTime opening = businessHours.opening();
        LocalTime closing = businessHours.closing();

        // then
        assertThat(opening).isEqualTo(new LocalTime(10,30));
        assertThat(closing).isEqualTo(new LocalTime(0,0));
    }

    @Test
    public void testWeekendHoursAreCorrect_bothInPMAndColon() {
        // given
        businessHours = new BusinessHours(WEEKDAY_DAYS, "1:30 pm–11:59 pm");

        // when
        LocalTime opening = businessHours.opening();
        LocalTime closing = businessHours.closing();

        // then
        assertThat(opening).isEqualTo(new LocalTime(13,30));
        assertThat(closing).isEqualTo(new LocalTime(23,59));
    }

    @Test
    public void testWeekendHoursAreCorrect_bothHaveSameHourAndColon() {
        // given
        businessHours = new BusinessHours(WEEKDAY_DAYS, "1:30am–1:30 pm");

        // when
        LocalTime opening = businessHours.opening();
        LocalTime closing = businessHours.closing();

        // then
        assertThat(opening).isEqualTo(new LocalTime(1,30));
        assertThat(closing).isEqualTo(new LocalTime(13,30));
    }

    @Test
    public void testStartTimeForWeekdayShows() {
        // given
        businessHours = new BusinessHours(WEEKDAY_DAYS, WEEKDAY_HOURS);

        // when
        LocalTime startTimeForWeekdayShows = businessHours.startTimeForAllShowings();

        // then
        assertThat(startTimeForWeekdayShows).isEqualTo(new LocalTime(11,15));
    }

    @Test
    public void testStartTimeForWeekendShows() {
        // given
        businessHours = new BusinessHours(WEEKEND_DAYS, WEEKEND_HOURS);

        // when
        LocalTime startTimeForWeekdayShows = businessHours.startTimeForAllShowings();

        // then
        assertThat(startTimeForWeekdayShows).isEqualTo(new LocalTime(10,45));
    }
}
