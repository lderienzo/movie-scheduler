package com.tandem.showtime.moviescheduler.hours;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tandem.showtime.moviescheduler.hours.BusinessHours;

public class WeekendHours extends BusinessHours {

    @JsonCreator
    public WeekendHours(@JsonProperty("days") String days, @JsonProperty("hours") String hours) {
        super(days, hours);
    }
}
