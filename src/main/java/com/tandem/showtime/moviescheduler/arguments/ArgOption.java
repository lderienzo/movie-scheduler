package com.tandem.showtime.moviescheduler.arguments;

public enum ArgOption {
    HOURS_FILE("hours_file"),
    MOVIES_FILE("movies_file"),
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
