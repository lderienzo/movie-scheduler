package com.tandem.showtime.moviescheduler.schedule;

import org.joda.time.LocalTime;

public class ScheduleUtils {

    public static LocalTime formatTimeForSchedule(LocalTime unformattedTime) {
        LocalTime easyToReadScheduleTime = null;
        if (minutesAreNotAnIncrementOfFive(unformattedTime)) {
            int minutesForSchedule = makeTimeFormatEasyToRead(unformattedTime);
            easyToReadScheduleTime = new LocalTime(unformattedTime.getHourOfDay(), minutesForSchedule);
        }
        return easyToReadScheduleTime;
    }

    private static int makeTimeFormatEasyToRead(LocalTime unformattedTime) {
        return roundTimeDownToAnIncrementOfFiveMinutes(unformattedTime);
    }

    private static int roundTimeDownToAnIncrementOfFiveMinutes(LocalTime unformattedTime) {
        int remainder = performModulusOperationOnUnformattedTime(unformattedTime);
        return subtractRemainderMinutesFromUnformattedTimeMinutes(unformattedTime, remainder);
    }

    private static int performModulusOperationOnUnformattedTime(LocalTime unformattedTime) {
        return unformattedTime.getMinuteOfHour() % 5;
    }

    private static int subtractRemainderMinutesFromUnformattedTimeMinutes(LocalTime unformattedTime, int remainder) {
        return unformattedTime.getMinuteOfHour() - remainder;
    }

    private static boolean minutesAreNotAnIncrementOfFive(LocalTime unformattedTime) {
        return unformattedTime.getMinuteOfHour() % 5 != 0;
    }
}
