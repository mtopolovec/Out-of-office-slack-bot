package com.slack.out_of_office_bot.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Current time, start of day for today and end of day for today
 */
public class DateTimeUtils {

    /**
     * Representing current date and time when invoked
     * @return current date and time
     */
    public static LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

    /**
     * Representing current date but with start of today's day e.g. 10.05.2020 | 00:00:00
     * @return start of today
     */
    public static LocalDateTime startOfDay() {
        return currentTime().with(LocalTime.MIN);
    }

    /**
     * Representing current date but with end of today's day e.g. 10.05.2020 | 23:59:59
     * @return end of today
     */
    public static LocalDateTime endOfDay() {
        return currentTime().with(LocalTime.MAX);
    }
}
