package com.tandem.showtime.moviescheduler;

public class BusinessHours {

    private String days;
    private String hours;

    public BusinessHours(String days, String hours) {
        this.days = days;
        this.hours = hours;
    }

    public String from() {
        return "";
    }

    public String to() {
        return "";
    }

    public int opensAt() {
        return 0;
    }

    public int closesAt() {
        return 0;
    }
}
