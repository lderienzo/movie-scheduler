package com.tandem.showtime.moviescheduler.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;


public class Movies {
    private List<Movie> playing;

    @JsonCreator
    public Movies (@JsonProperty("playing") List<Movie> playing) {
        this.playing = playing;
    }

    public Movies() {}

    public List<Movie> playing() {
        return ImmutableList.copyOf(playing);
    }
}
