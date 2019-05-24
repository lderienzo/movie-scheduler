package com.tandem.showtime.moviescheduler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Movies {
    private List<Movie> playing; // TODO: make immutable after construction?

    @JsonCreator
    public Movies (@JsonProperty("playing") List<Movie> playing) {
        this.playing = playing;
    }

    public Movies() {}

    public List<Movie> playing() {
        return playing;
    }
}
