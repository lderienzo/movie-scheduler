package com.tandem.showtime.moviescheduler;



import org.joda.time.LocalTime;



public class Showing {
    private LocalTime startTime;
    private LocalTime endTime;

    public Showing(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
