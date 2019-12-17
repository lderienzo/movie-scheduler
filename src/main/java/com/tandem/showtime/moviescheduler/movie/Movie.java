package com.tandem.showtime.moviescheduler.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.tandem.showtime.moviescheduler.exceptions.MovieException;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Movie {
    private String title;
    private String rating;
    private String length;
    private int minutes;
    private String lengthAsMinutesString;
    private String scheduleTitle;
    private String error;
    private final List<Showing> weekdayShowings = new ArrayList<>();
    private final List<Showing> weekendShowings = new ArrayList<>();
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d{2,3})?");
    private static final Logger LOG = LoggerFactory.getLogger(Movie.class);

    @JsonCreator
    public Movie(@JsonProperty("title") String title, @JsonProperty("rating") String rating, @JsonProperty("length") String length) {
        setMembers(title, rating, length);
        formTitleForSchedule();
        convertLengthFromStringToInt();
    }

    private void setMembers(String movieTitle, String movieRating, String movieLength) {
        title = movieTitle;
        rating = movieRating;
        length = movieLength;
    }

    public String title() {
        return title;
    }

    private void formTitleForSchedule() {
        String movieName = removeMovieYearFromTitle();
        String ratingSymbol = obtainRatingSymbol();
        scheduleTitle = movieName + " " + ratingSymbol;
    }

    private String removeMovieYearFromTitle() {
        String[] separateMovieNameAndYear = splitMovieYearFromMovieName();
        if (splitTitleIsInvalid(separateMovieNameAndYear)) {
            LOG.error("ERROR >>> error removing year from title.");
            throw new MovieException(error);
        }
        return justMovieName(separateMovieNameAndYear);
    }

    private String[] splitMovieYearFromMovieName() {
        return title.split("\\(");
    }

    private boolean splitTitleIsInvalid(String[] splitTitle) {
        return splitTitle == null || splitTitle.length != 2;
    }

    private String justMovieName(String[] splitTitle) {
        String movieName = splitTitle[0];
        return movieName.trim();
    }

    private String obtainRatingSymbol() {
        return obtainRatingSymbolWithParenthesis();
    }

    private String obtainRatingSymbolWithParenthesis() {
        String ratingSymbolWithPeriod = removeRatedTextFromRating();
        String bareRatingSymbol = removePeriodFromEndOfRating(ratingSymbolWithPeriod);
        return encaseRatingSymbolInParenthesis(bareRatingSymbol);
    }

    private String removeRatedTextFromRating() {
        String[] splitRating = rating.split("Rated ");
        if (splitRatingIsInvalid(splitRating)) {
            logError("ERROR >>> error processing movie rating");
            throw new MovieException(error);
        }
        return symbolPortionOfRatingWithPeriod(splitRating);
    }

    private boolean splitRatingIsInvalid(String[] splitRating) {
        return splitRating == null || splitRating.length != 2;
    }

    private void logError(String errorMsg) {
        setErrorMemberWithErrorMessage(errorMsg);
        LOG.error(error);
    }

    private void setErrorMemberWithErrorMessage(String errorMsg) {
        error = errorMsg;
    }

    private String symbolPortionOfRatingWithPeriod(String[] splitRating) {
        return splitRating[1];
    }

    private String removePeriodFromEndOfRating(String ratingSymbolWithPeriod) {
        return ratingSymbolWithPeriod.replace(".", "");
    }

    private String encaseRatingSymbolInParenthesis(String ratingSymbol) {
        return "(" + ratingSymbol + ")";
    }

    public int length() {
        convertLengthFromStringToInt();
        return minutes;
    }

    private void convertLengthFromStringToInt() {
        extractMinutesFromLengthString();
        if (extractedLengthStringIsInvalid()) {
            LOG.error("ERROR >>> error processing movie length.");
            throw new MovieException(error);
        }
        minutes = Integer.valueOf(lengthAsMinutesString).intValue();
    }

    private void extractMinutesFromLengthString() {
        Matcher numberMatcher = NUMBER_PATTERN.matcher(length);
        if (matcherCannotFindMinutesInLengthString(numberMatcher)) {
            LOG.error("ERROR >>> error processing movie length.");
            throw new MovieException(error);
        }
        lengthAsMinutesString = numberMatcher.group(1);
    }

    private boolean matcherCannotFindMinutesInLengthString(Matcher matcher) {
        return !matcher.find();
    }

    private boolean extractedLengthStringIsInvalid() {
        return Strings.isNullOrEmpty(lengthAsMinutesString);
    }

    public String titleWithRatingForSchedule() {
        return scheduleTitle;
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
