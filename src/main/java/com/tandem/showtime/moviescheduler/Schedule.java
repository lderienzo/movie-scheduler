package com.tandem.showtime.moviescheduler;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Showing> schedule;

    public Schedule(int showings) {
        schedule = new ArrayList<>(showings);
    }

    public List<Showing> get() {
        return schedule;
    }
}
