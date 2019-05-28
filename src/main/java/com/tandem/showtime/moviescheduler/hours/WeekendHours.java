package com.tandem.showtime.moviescheduler.hours;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeekendHours extends BusinessHours {

    @JsonCreator
    public WeekendHours(@JsonProperty("days") String days, @JsonProperty("hours") String hours) {
        super(days, hours);
    }
}
