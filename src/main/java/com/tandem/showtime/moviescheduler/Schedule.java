package com.tandem.showtime.moviescheduler;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Showing> schedule;

    public Schedule(int expectedNumberOfShowingsForSchedule) {
        schedule = new ArrayList<>(expectedNumberOfShowingsForSchedule);
    }

    public List<Showing> get() {
        return schedule;
    }
}
