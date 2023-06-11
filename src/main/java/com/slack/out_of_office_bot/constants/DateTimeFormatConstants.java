package com.slack.out_of_office_bot.constants;

import com.slack.out_of_office_bot.utility.DateTimeUtils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Types of date and time formats
 */
public class DateTimeFormatConstants {
    /**
     * Builder for DateTimeFormatter with year, month and day of month value where we only append to that hours and minutes
     */
    public static final DateTimeFormatter LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH")
            .optionalStart()
            .appendPattern(":mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.YEAR, DateTimeUtils.currentTime().getYear())
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, DateTimeUtils.currentTime().getMonthValue())
            .parseDefaulting(ChronoField.DAY_OF_MONTH, DateTimeUtils.currentTime().getDayOfMonth())
            .toFormatter(Locale.ENGLISH);

    /**
     * Builder for DateTimeFormatter with year only where we append day of the month and month
     */
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd.MM")
            .optionalStart()
            .appendPattern(".")
            .optionalEnd()
            .parseDefaulting(ChronoField.YEAR, DateTimeUtils.currentTime().getYear())
            .toFormatter(Locale.ENGLISH);

    /**
     * Representing a date and time format
     */
    public static final DateTimeFormatter MESSAGE_DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm");

    /**
     * Representing a time format
     */
    public static final DateTimeFormatter MESSAGE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Representing a date format
     */
    public static final DateTimeFormatter MESSAGE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");
}
