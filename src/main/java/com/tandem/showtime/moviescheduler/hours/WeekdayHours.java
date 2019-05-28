package com.tandem.showtime.moviescheduler.hours;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tandem.showtime.moviescheduler.hours.BusinessHours;

public class WeekdayHours extends BusinessHours {

    @JsonCreator
    public WeekdayHours(@JsonProperty("days") String days, @JsonProperty("hours") String hours) {
        super(days, hours);
    }
}
