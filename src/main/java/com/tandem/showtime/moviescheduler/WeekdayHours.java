package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeekdayHours extends BusinessHours {

    @JsonCreator
    public WeekdayHours(@JsonProperty("days") String days, @JsonProperty("hours") String hours) {
        super(days, hours);
    }
}
