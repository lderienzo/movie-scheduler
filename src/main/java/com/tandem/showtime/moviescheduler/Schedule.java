package com.tandem.showtime.moviescheduler;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Movie> schedule;

    public Schedule(int showings) {
        schedule = new ArrayList<>(showings);
    }

    public Schedule() {}

    // TODO:  can we make this an immutable list?
    public List<Movie> moviesPlaying() {
        return schedule;
    }
}
