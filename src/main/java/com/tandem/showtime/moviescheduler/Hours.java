package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hours {

    private WeekdayHours weekdayHours;
    private WeekendHours weekendHours;

    @JsonCreator
    public Hours(@JsonProperty("weekdayHours") WeekdayHours weekday, @JsonProperty("weekendHours") WeekendHours weekendHours) {
        this.weekdayHours = weekday;
        this.weekendHours = weekendHours;
    }

    public Hours(){}

    public WeekdayHours weekday() {
        return weekdayHours;
    }

    public WeekendHours weekend() {
        return weekendHours;
    }
}


