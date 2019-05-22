package com.tandem.showtime.moviescheduler;

public class Movie {
    private int length;
    private String titleWithRating;

    public Movie(String movieInfoString) {
        extractMovieLengthFromInfoString();
        extractMovieTitleWithRatingFromInfoString();
    }

    private int extractMovieLengthFromInfoString() {
        return 0;
    }

    private String extractMovieTitleWithRatingFromInfoString() {
        return "";
    }

    public int length() {
        return length;
    }

    public String getTitleWithRating() {
        return titleWithRating;
    }
}
