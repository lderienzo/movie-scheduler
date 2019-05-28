package com.tandem.showtime.moviescheduler.schedule;

import java.util.ArrayList;
import java.util.List;

import com.tandem.showtime.moviescheduler.movie.Movie;

public class Schedule {
    private List<Movie> schedule;

    public Schedule(int showings) {
        schedule = new ArrayList<>(showings);
    }

    public List<Movie> moviesPlaying() {
        return schedule;
    }
}
