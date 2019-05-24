package com.tandem.showtime.moviescheduler;

public enum ArgOption {
    MOVIE_FILE("movies_file"),
    HOURS_FILE("hours_file"),;

    private String argName;

    ArgOption(String argName) {
        this.argName = argName;
    }

    @Override
    public String toString() {
        return argName;
    }
}
