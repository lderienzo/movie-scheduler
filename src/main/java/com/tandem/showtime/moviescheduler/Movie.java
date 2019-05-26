package com.tandem.showtime.moviescheduler;

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
    private List<Showing> weekdayShowings = new ArrayList<>();
    private List<Showing> weekendShowings = new ArrayList<>();

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
        String[] splitTitle = title.split("\\(");
        if (splitTitle == null || splitTitle.length != 2) {
            // TODO: ERROR
        }
        String alteredTitle = splitTitle[0].trim();

        String[] splitRating = rating.split("Rated ");
        if (splitRating == null || splitRating.length != 2) {
            // TODO: ERROR
        }

        String ratingSymbol = splitRating[1].replace(".", "");
        ratingSymbol = "(" + ratingSymbol + ")";

        titleWithRatingForSchedule = alteredTitle + " " + ratingSymbol;
        return titleWithRatingForSchedule;
    }

    public void addWeekdayShowing(Showing showing) {
        weekdayShowings.add(showing);
    }

    public List<Showing> weekdayShowings() {
        return ImmutableList.copyOf(weekdayShowings);
    }

    public void addWeekendShowing(Showing showing) {
        weekendShowings.add(showing);
    }

    public List<Showing> weekendShowings() {
        return ImmutableList.copyOf(weekendShowings);
    }
}
