package com.tandem.showtime.moviescheduler.hours;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tandem.showtime.moviescheduler.exceptions.MovieException;

// TODO -- MAJORLY REFACTOR THIS!
public class BusinessHours extends RuntimeException {
    private String days;
    private String hours;
    private String error;
    private LocalTime openingLocalTime;
    private LocalTime closingLocalTime;
    private static final int MINUTES_REQUIRED_TILL_FIRST_SHOWING = 15;
    private static final String ERROR_PROCESSING_HOURS_MSG = "ERROR >>> error processing business hours.";
    private static final Logger LOG = LoggerFactory.getLogger(BusinessHours.class);


    public BusinessHours(String days, String hours) {
        setDaysAndHoursMembers(days, hours);
        convertBusinessHoursToLocalDateTimeObjects();
    }

    private void setDaysAndHoursMembers(String days, String hours) {
        this.days = days;
        this.hours = hours.toLowerCase();
    }

    public LocalTime opening() {
        return openingLocalTime;
    }

    public LocalTime closing() {
        return closingLocalTime;
    }

    public LocalTime startTimeForAllShowings() {
        return openingLocalTime.plusMinutes(MINUTES_REQUIRED_TILL_FIRST_SHOWING);
    }

    // TODO: CLEAN THIS UP!
    private void convertBusinessHoursToLocalDateTimeObjects() {
        String regExToSeparateBusinessHours = "((\\d{1,2}(?:\\:\\d{2})?)\\s*(am|pm))+";
        Pattern patternToSeparateBusinessHours = Pattern.compile(regExToSeparateBusinessHours);
        Matcher matcherToFindSeparateBusinessHours = patternToSeparateBusinessHours.matcher(hours);
        String openingHourWithAmPmValue = "";
        String closingHourWithAmPmValue = "";
        if (matcherToFindSeparateBusinessHours.find())
            openingHourWithAmPmValue = matcherToFindSeparateBusinessHours.group(0);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        String tempHours = hours;
        String hoursWithClosingHoursRemaining = tempHours.replace(openingHourWithAmPmValue, "");
        matcherToFindSeparateBusinessHours = patternToSeparateBusinessHours.matcher(hoursWithClosingHoursRemaining);
        if (matcherToFindSeparateBusinessHours.find())
            closingHourWithAmPmValue = matcherToFindSeparateBusinessHours.group(1);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        String regExToSeparateHourFromAmPm = "(\\d{1,2}(?:\\:\\d{2})?)";
        Pattern patternToSeparateHourFromAmPm = Pattern.compile(regExToSeparateHourFromAmPm);
        Matcher matcherToFindOpeningHourValue = patternToSeparateHourFromAmPm.matcher(openingHourWithAmPmValue);
        String openingHourValue = "";
        if (matcherToFindOpeningHourValue.find())
            openingHourValue = matcherToFindOpeningHourValue.group(0);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        Matcher matcherToFindClosingHourValue = patternToSeparateHourFromAmPm.matcher(closingHourWithAmPmValue);
        String closingHourValue = "";
        if (matcherToFindClosingHourValue.find())
            closingHourValue = matcherToFindClosingHourValue.group(0);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        String regExToAmPmFromHour = "(am|pm)";
        Pattern patternToSeparateAmPmFromHour = Pattern.compile(regExToAmPmFromHour);
        Matcher matcherForFindingOpeningAmPmValue = patternToSeparateAmPmFromHour.matcher(openingHourWithAmPmValue);
        String openingAmPmValue = "";
        if (matcherForFindingOpeningAmPmValue.find())
            openingAmPmValue = matcherForFindingOpeningAmPmValue.group(0);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        Matcher matcherForFindingClosingAmPmValue = patternToSeparateAmPmFromHour.matcher(closingHourWithAmPmValue);
        String closingAmPmValue = "";
        if (matcherForFindingClosingAmPmValue.find())
            closingAmPmValue = matcherForFindingClosingAmPmValue.group(0);
        else {
            logError(ERROR_PROCESSING_HOURS_MSG);
            throw new MovieException(error);
        }

        BusinessHour opening = new BusinessHour(openingHourValue, openingAmPmValue);
        BusinessHour closing = new BusinessHour(closingHourValue, closingAmPmValue);
        openingLocalTime = opening.getLocalTimeRepresentation();
        closingLocalTime = closing.getLocalTimeRepresentation();
    }

    private void logError(String errorMsg) {
        setErrorMemberWithErrorMessage(errorMsg);
        LOG.error(error);
    }

    private void setErrorMemberWithErrorMessage(String errorMsg) {
        error = errorMsg;
    }

    class BusinessHour {
        private final String hourWithAmPm;
        private final String amPm;

        BusinessHour(String hourStr, String amPmStr) {
            hourWithAmPm = hourStr;
            amPm = amPmStr;
        }

        private LocalTime getLocalTimeRepresentation() {
            return convertHourStringToLocalTimeObject();
        }

        private LocalTime convertHourStringToLocalTimeObject() {
            String[] splitHour = splitHourIntoHoursAndMinutes(hourWithAmPm);
            int hour = convertHourToInt(splitHour[0]);
            hour = convertHourIntTo24HourFormat(hour);
            int minute = 0;
            if (hourContainsMinutes()) {
                minute = convertMinuteToInt(splitHour[1]);
            }
            return new LocalTime(hour, minute);
        }

        private String[] splitHourIntoHoursAndMinutes(String hour) {
            return hour.split(":");
        }

        private int convertHourToInt(String hour) {
            return convertStringToInt(hour);
        }

        private int convertHourIntTo24HourFormat(int hour) {
            if (hour == 12 && amPm.equals("am")) {
                hour = 0;
            }
            else if (hour >= 1 && hour < 12 && amPm.equals("pm")) {
                hour = hour + 12;
            }
            return hour;
        }

        private boolean hourContainsMinutes() {
            return hourWithAmPm.contains(":");
        }

        private int convertMinuteToInt(String minute) {
            return convertStringToInt(minute);
        }

        private int convertStringToInt(String stringToConvert) {
            return Integer.parseInt(stringToConvert);
        }
    }
}
