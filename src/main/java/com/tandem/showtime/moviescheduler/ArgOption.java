package com.tandem.showtime.moviescheduler;

public enum ArgOption {

    MOVIES_FILE("movies_file"),
    HOURS_FILE("hours_file"),
    SCHEDULE_FILE("schedule_file");

    private String argName;

    ArgOption(String argName) {
        this.argName = argName;
    }

    @Override
    public String toString() {
        return argName;
    }
}
