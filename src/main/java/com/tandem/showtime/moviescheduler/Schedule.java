package com.tandem.showtime.moviescheduler;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Movie> schedule;

    public Schedule(int showings) {
        schedule = new ArrayList<>(showings);
    }

    public List<Movie> get() {
        return schedule;
    }
}
