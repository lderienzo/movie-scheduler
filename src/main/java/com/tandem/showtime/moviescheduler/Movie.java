package com.tandem.showtime.moviescheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {

    private String title;
    private String rating;
    private String length; // TODO: should we rename this to "runningTime"?
    private String titleWithRatingForSchedule;


    @JsonCreator
    public Movie(@JsonProperty("title") String title,
                 @JsonProperty("rating") String rating,
                 @JsonProperty("length") String length) {
        this.title = title;
        this.rating = rating;
        this.length = length;
        extractMovieLengthFromInfoString();
        extractMovieTitleWithRatingFromInfoString();
    }

    private int extractMovieLengthFromInfoString() {
        return 0;
    }

    private String extractMovieTitleWithRatingFromInfoString() {
        return "";
    }

    public String length() {
        return length;
    }

    public String titleWithRatingForSchedule() {
        return titleWithRatingForSchedule;
    }
}
