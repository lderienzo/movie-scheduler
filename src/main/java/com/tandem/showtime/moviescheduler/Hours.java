package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hours {

    private Weekday weekday;
    private Weekend weekend;

    @JsonCreator
    public Hours(@JsonProperty("weekday") Weekday weekday, @JsonProperty("weekend") Weekend weekend) {
        this.weekday = weekday;
        this.weekend = weekend;
    }

    public Weekday weekday() {
        return weekday;
    }

    public Weekend weekend() {
        return weekend;
    }
}


