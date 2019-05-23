package com.tandem.showtime.moviescheduler;

public enum ArgOption {
    MOVIE_FILE("movie_file");

    private String argName;

    ArgOption(String argName) {
        this.argName = argName;
    }

    @Override
    public String toString() {
        return argName;
    }
}
