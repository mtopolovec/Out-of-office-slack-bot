package com.slack.out_of_office_bot.parsers;

import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TimeDateIntervalExtractorTest {

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsTimeOnly() {

        String message = "12:30 - 15:45 Lunch break";
        int startTimeHour = 12;
        int startTimeMin = 30;
        int endTimeHour = 15;
        int endTimeMin = 45;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTimeHour));
        assertThat(actualDates.getFrom().getMinute(), equalTo(startTimeMin));
        assertThat(actualDates.getTo().getHour(), equalTo(endTimeHour));
        assertThat(actualDates.getTo().getMinute(), equalTo(endTimeMin));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsShortTimeOnly() {

        String message = "12 - 15 Lunch break";
        int startTime = 12;
        int endTime = 15;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTime));
        assertThat(actualDates.getTo().getHour(), equalTo(endTime));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsTimeWithKeywordTomorrow() {

        String message = "tomorrow 12:30 - 15:45 Lunch break";
        int startTimeHour = 12;
        int startTimeMin = 30;
        int endTimeHour = 15;
        int endTimeMin = 45;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTimeHour));
        assertThat(actualDates.getFrom().getMinute(), equalTo(startTimeMin));
        assertThat(actualDates.getTo().getHour(), equalTo(endTimeHour));
        assertThat(actualDates.getTo().getMinute(), equalTo(endTimeMin));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo((DateTimeUtils.currentTime().plusDays(1).getDayOfMonth())));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo((DateTimeUtils.currentTime().plusDays(1).getDayOfMonth())));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsShortTimeWithKeywordTomorrow() {

        String message = "tomorrow 12 - 15 Lunch break";
        int startTime = 12;
        int endTime = 15;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTime));
        assertThat(actualDates.getTo().getHour(), equalTo(endTime));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo((DateTimeUtils.currentTime().plusDays(1).getDayOfMonth())));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo((DateTimeUtils.currentTime().plusDays(1).getDayOfMonth())));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsLongTimeWithDateInFuture() {

        String message = "31.12 12:00 - 14:00 Lunch break";
        int dayOfMonth = 31;
        int month = 12;
        int startTime = 12;
        int endTime = 14;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getFrom().getMonthValue(), equalTo(month));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getTo().getMonthValue(), equalTo(month));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTime));
        assertThat(actualDates.getTo().getHour(), equalTo(endTime));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsShortTimeWithDateInFuture() {

        String message = "31.12 12 - 15 Lunch break";
        int dayOfMonth = 31;
        int month = 12;
        int startTime = 12;
        int endTime = 15;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getFrom().getMonthValue(), equalTo(month));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getTo().getMonthValue(), equalTo(month));
        assertThat(actualDates.getFrom().getHour(), equalTo(startTime));
        assertThat(actualDates.getTo().getHour(), equalTo(endTime));
    }

    @Test
    void extractInterval_shouldReturnStartDateAndEndDateWhenInputIsDateOnly() {

        String message = "10.08 - 25.08 Lunch break";
        int startDate = 10;
        int endDate = 25;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo(startDate));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo(endDate));
    }

    @Test
    void extractInterval_shouldReturnDateWhenInputIsWholeDay() {

        String message = "25.09 Doctor appointment";
        int dayOfMonth = 25;
        int month = 9;

        DateTimeInterval actualDates = TimeDateIntervalExtractor.extractInterval(message);

        assertThat(actualDates, is(notNullValue()));
        assertThat(actualDates.getFrom().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getFrom().getMonthValue(), equalTo(month));
        assertThat(actualDates.getTo().getDayOfMonth(), equalTo(dayOfMonth));
        assertThat(actualDates.getTo().getMonthValue(), equalTo(month));
    }

}