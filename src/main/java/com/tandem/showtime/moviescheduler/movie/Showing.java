package com.tandem.showtime.moviescheduler.movie;



import org.joda.time.LocalTime;



public class Showing {
    private LocalTime startTime;
    private LocalTime endTime;

    public Showing(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Showing() {}

    public LocalTime startTime() {
        return startTime;
    }

    public LocalTime endTime() {
        return endTime;
    }
}
