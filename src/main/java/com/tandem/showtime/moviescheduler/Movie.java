package com.tandem.showtime.moviescheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

// TODO: clean up
public class Movie {

    private String title;
    private String rating;
    private String length;
    // TODO: would like Jackson to ignore these if possible
    private int lengthAsInt;
    private String extractedLengthStr;
    private String titleWithRatingForSchedule;
    private List<Showing> showings = new ArrayList<>();     // TODO: how to expose this without allowing client to directly manipulate

    @JsonCreator
    public Movie(@JsonProperty("title") String title,
                 @JsonProperty("rating") String rating,
                 @JsonProperty("length") String length) {
        this.title = title;
        this.rating = rating;
        this.length = length;
        extractMovieTitleWithRatingFromInfoString();
        convertMinutesFromStringToInt();
    }

    public String title() {
        return title;
    }

    private String extractMovieTitleWithRatingFromInfoString() {
        return "";
    }

    public int length() {
        convertMinutesFromStringToInt();
        return lengthAsInt;
    }

    private void convertMinutesFromStringToInt() {
        extractMovieLengthString();
        if (!Strings.isNullOrEmpty(extractedLengthStr)) {
            this.lengthAsInt = Integer.valueOf(extractedLengthStr).intValue();
        }
        else {
            // TODO: error
        }
    }

    private void extractMovieLengthString() {
        Pattern pattern = Pattern.compile("(\\d{2,3})?");
        Matcher matcher = pattern.matcher(length);
        if (matcher.find())
            this.extractedLengthStr = matcher.group(1);
        else {
            //TODO: throw exception
        }
    }


    public String titleWithRatingForSchedule() {
        // TODO:
        return titleWithRatingForSchedule;
    }

    public void addShowing(Showing showing) {
        showings.add(showing);
    }

    public List<Showing> getShowings() {
        return ImmutableList.copyOf(showings);
    }
}
