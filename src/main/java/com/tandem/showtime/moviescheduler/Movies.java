package com.tandem.showtime.moviescheduler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Movies {
    private List<Movie> movieList; // TODO: make immutable after construction?

    @JsonCreator
    public Movies (@JsonProperty("movieList") List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> get() {
        return movieList;
    }
}
