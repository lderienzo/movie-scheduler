package com.tandem.showtime.moviescheduler;

import lombok.Getter;

@Getter
public class HoursOfOperation {
    private WeekdayHours weekdayHours;
    private String weedendHours;

    public HoursOfOperation(String hoursOfOperation) {
        //  TODO: parce weekday and weedend hours out of hours of operation
    }

}
