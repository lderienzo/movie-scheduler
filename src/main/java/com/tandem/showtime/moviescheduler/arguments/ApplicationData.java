package com.tandem.showtime.moviescheduler.arguments;

import com.tandem.showtime.moviescheduler.hours.Hours;
import com.tandem.showtime.moviescheduler.movie.Movies;

public class ApplicationData {
    private Hours hours;
    private Movies movies;
    private String outFilePath;

    ApplicationData(Hours hours, Movies movies, String outFilePath) {
        this.hours = hours;
        this.movies = movies;
        this.outFilePath = outFilePath;
    }

    public Movies getMovies() {
        return movies;
    }

    public Hours getHours() {
        return hours;
    }

    public String getOutFilePath() {
        return outFilePath;
    }
}
