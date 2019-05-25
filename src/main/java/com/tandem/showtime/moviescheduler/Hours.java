package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hours {

    private WeekdayHours weekdayHours;
    private Weekend weekend;

    @JsonCreator
    public Hours(@JsonProperty("weekdayHours") WeekdayHours weekday, @JsonProperty("weekend") Weekend weekend) {
        this.weekdayHours = weekday;
        this.weekend = weekend;
    }

    public Hours(){}

    public WeekdayHours weekday() {
        return weekdayHours;
    }

    public Weekend weekend() {
        return weekend;
    }
}


