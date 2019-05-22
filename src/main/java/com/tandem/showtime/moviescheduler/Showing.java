package com.tandem.showtime.moviescheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Showing {
    private int startTime;
    private int endTime;
    private Movie movie;
}
