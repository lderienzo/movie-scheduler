package com.tandem.showtime.moviescheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalTime;

import com.google.common.base.Strings;
// TODO: clean up
public class BusinessHours extends RuntimeException {
    private static final int MINUTES_REQURIED_TILL_FIRST_SHOWING = 15;
    private static final String HOUR_PATTERN = "(\\d{1,2}(?:\\:\\d{2})?)";
    private static final Pattern pattern = Pattern.compile(HOUR_PATTERN);
    private String days;
    private String hours;
    private String opening;
    private String closing;
    private LocalTime openingLocalTime;
    private LocalTime closingLocalTime;
    boolean isOpening = true;

    public BusinessHours(String days, String hours) {
        setDaysAndHours(days, hours);
        splitHoursIntoOpeningAndClosingTimes();
    }

    private void setDaysAndHours(String days, String hours) {
        this.days = days;
        this.hours = hours;
    }

    private void splitHoursIntoOpeningAndClosingTimes() {
        extractOpeningAndClosingStringsWithAmOrPM();
        convertOpeningHourToLocalTime();
        isOpening = false;
        convertClosingHourToLocalTime();
    }

    private void extractOpeningAndClosingStringsWithAmOrPM() {
        opening = extractHourWith(hours);
        // can use opening as a match to replace all occurrences, then use regex to look for numbers. if no numbers then they where the same number
        String hoursWithOpeningTimeRemoved = hours.replaceAll(opening, "");

        Pattern hoursWithOpeningTimeRemovedPattern = Pattern.compile("\\d{2}");
        Matcher hoursWithOpeningTimeRemovedMatcher = hoursWithOpeningTimeRemovedPattern.matcher(hoursWithOpeningTimeRemoved);
        // hours DO contain the same number for opening and closing times
        if (!hoursWithOpeningTimeRemovedMatcher.find()) {  // no hour numbers present after replace, we have the same number for closing as we do opening.
            closing = extractHourWith(opening);
        }
        else { // hour number present after replace. hours DO NOT contain the same number for opening and closing
            closing = extractHourWith(hoursWithOpeningTimeRemoved);
        }
    }

    private void convertOpeningHourToLocalTime() {
        convertHourToLocalTime(opening);
    }

    private void convertClosingHourToLocalTime() {
        convertHourToLocalTime(closing);
    }

    private String extractHourWith(String hours) {
        Matcher matcher = pattern.matcher(hours.trim().toLowerCase());

        String extractedHour = "";
        if (matcher.find())
            extractedHour = matcher.group(1);

        return extractedHour;
    }

    private void convertHourToLocalTime(String hour) {

        if (!Strings.isNullOrEmpty(hour)) {

            int hourInt = 0;
            int minuteInt = 0;
            if (hour.contains(":")) {
                // split closing hour and minutes
                String[] splitHour = hour.split(":");
                if (splitHour != null && splitHour.length == 2) {
                    String hours = splitHour[0];
                    String minutes = splitHour[1];

                    if (!Strings.isNullOrEmpty(hours)) {
                        hourInt = Integer.parseInt(hours);
                    }

                    if (!Strings.isNullOrEmpty(minutes)) {
                        minuteInt = Integer.parseInt(minutes);
                    }
                }
            }
            else {
                hourInt = Integer.parseInt(hour);
            }

            if (isOpening) {
                openingLocalTime = new LocalTime(hourInt, minuteInt);
            } else {
                int _24hourTime = 0;
                if (hourInt != 12)
                    _24hourTime = hourInt + 12;
                closingLocalTime = new LocalTime(_24hourTime, minuteInt);
            }
        }
    }

    public LocalTime opening() {
        return openingLocalTime;
    }

    public LocalTime closing() {
        return closingLocalTime;
    }

    public LocalTime startTimeForAllShowings() {
        return openingLocalTime.plusMinutes(MINUTES_REQURIED_TILL_FIRST_SHOWING);
    }
}
