package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Weekend extends BusinessHours {

    @JsonCreator
    public Weekend(@JsonProperty("days") String days, @JsonProperty("hours") String hours) {
        super(days, hours);
    }
}
