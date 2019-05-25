package com.tandem.showtime.moviescheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalTime;

import com.google.common.base.Strings;

public class BusinessHours extends RuntimeException {
    private static final int MINUTES_REQURIED_TILL_FIRST_SHOWING = 15;
    private static final String HOUR_PATTERN = "(\\d{1,2}(?:\\:\\d{2})?)";
    private String days;
    private String hours;
    private String opening;
    private String closing;
    private String splitClosing;
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

        // check if opening and closing hour values are the same
        int openingIndex = hours.lastIndexOf(opening);

        int hoursLength = hours.length();
        if (hoursLength - 4 == openingIndex) {  // we have the same number
            closing = extractHourWith(opening);
        }
        else {

            // now split hours with opening value closing get remaining closing hours
            String[] splitHoursWithOnlyClosingTimeRemaining = hours.split(opening); // TODO: doesn't work if opening & closing hours are the same 'number'
            if (splitHoursWithOnlyClosingTimeRemaining != null &&
                    splitHoursWithOnlyClosingTimeRemaining.length == 2) {
                splitClosing = splitHoursWithOnlyClosingTimeRemaining[1];
            }

            closing = extractHourWith(splitClosing);
        }
    }

    private void convertOpeningHourToLocalTime() {
        convertHourToLocalTime(opening);
    }

    private void convertClosingHourToLocalTime() {
        convertHourToLocalTime(closing);
    }

    private String extractHourWith(String hours) {
        Pattern pattern = Pattern.compile(HOUR_PATTERN);
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

    public LocalTime startTimeForWeekdayShows() {
        return openingLocalTime.plusMinutes(MINUTES_REQURIED_TILL_FIRST_SHOWING);
    }
}
