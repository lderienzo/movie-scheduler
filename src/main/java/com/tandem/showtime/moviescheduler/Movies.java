package com.tandem.showtime.moviescheduler;

import java.util.List;

import lombok.Getter;


public class Movies {
    private List<Movie> movieList; // TODO: make immutable after construction?

    public List<Movie> get() {
        return movieList;
    }
}
