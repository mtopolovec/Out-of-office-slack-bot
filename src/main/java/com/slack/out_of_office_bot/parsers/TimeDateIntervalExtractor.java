package com.slack.out_of_office_bot.parsers;

import com.slack.out_of_office_bot.enums.MessageType;
import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.utility.DateTimeUtils;

import java.time.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.slack.out_of_office_bot.constants.DateTimeFormatConstants.*;
import static com.slack.out_of_office_bot.constants.RegexConstants.*;

public class TimeDateIntervalExtractor {

    private final static String KEYWORD = "tomorrow";

    /**
     * Check is message contains a tomorrow keyword.
     * @param message message
     * @return true if keyword is found
     */
    private static Boolean isMessageForTomorrow(String message) {
        String keywordRegexCheck = "(.*\\b" + KEYWORD + "\\b.*)+";

        Matcher matcher = Pattern.compile(keywordRegexCheck).matcher(message);
        return matcher.find();
    }

    /**
     * Checks if message has a date keyword
     * @param message message
     * @return true if message contains date
     */
    private static Boolean isMessageWithDateKeyword(String message) {
        return DATE_PATTERN.matcher(message).find();
    }

    /**
     * Tries to determine what type of message it is given.
     * @param message message that will be tested
     * @return one of {@link MessageType}
     * @throws IllegalArgumentException if message type is not recognized
     */
    private static MessageType determineMessageType(String message) {
        if (Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find()) {
            if (MessageType.TIME_INTERVAL.getPattern().matcher(message).find()) {
                return MessageType.TIME_INTERVAL;
            }
        }
        if (Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find()) {
            if (MessageType.DATE_INTERVAL.getPattern().matcher(message).find()) {
                return MessageType.DATE_INTERVAL;
            }
        }

        throw new IllegalArgumentException("Could not establish a type of message " + message);
    }

    /**
     * Extracts a time interval from a given message.
     *
     * @param message message
     * @return a new TimeInterval
     */
    private static DateTimeInterval extractTimeInterval(String message) {
        List<LocalDateTime> times = TIME_PATTERN
                .matcher(message)
                .results()
                .map(result -> LocalDateTime.parse(result.group(), LOCAL_TIME_FORMATTER))
                .collect(Collectors.toList());
        return new DateTimeInterval(times.get(0), times.get(1));
    }

    /**
     * Extracts a date interval from a given message.
     *
     * @param message message
     * @return a new DateInterval
     */
    private static DateTimeInterval extractDateInterval(String message) {
        List<LocalDate> times = DATE_PATTERN.matcher(message)
                .results()
                .map(result -> LocalDate.parse(result.group(), DATE_FORMATTER))
                .collect(Collectors.toList());
        if (times.size() == 1) {
            return new DateTimeInterval(times.get(0).atStartOfDay(), times.get(0).atTime(LocalTime.MAX).minusMinutes(1));
        } else {
            return new DateTimeInterval(times.get(0).atStartOfDay(), times.get(1).atTime(LocalTime.MAX).minusMinutes(1));
        }
    }

    /**
     * Add days to a given date interval
     *
     * @param interval date interval from and to date
     * @param numberOfDays number of days to increase the initial value
     * @return a new interval that has its inital date from and date to increased for numberOfDays
     */
    private static DateTimeInterval addDays(DateTimeInterval interval, int numberOfDays) {
        return new DateTimeInterval(interval.getFrom().plusDays(numberOfDays), interval.getTo().plusDays(numberOfDays));
    }

    /**
     * Get number of days between two dates
     *
     * @param dateFrom date from
     * @param dateTo date to
     * @return number of days
     */
    public static long getNumberOfDaysBetweenTwoDates(LocalDate dateFrom, LocalDate dateTo) {
        return Duration.between(dateFrom.atStartOfDay(), dateTo.atStartOfDay()).toDays();
    }

    /**
     * Extract date and time interval from String message
     *
     * @param message users input string that contains String date or time value
     * @return a new date or time interval that is extracted from the String message
     */
    public static DateTimeInterval extractInterval(String message) {
        MessageType messageType = determineMessageType(message);

        if (MessageType.TIME_INTERVAL.equals(messageType)) {
            if (isMessageForTomorrow(message)) {
                return addDays(extractTimeInterval(message), 1);
            } else if (isMessageWithDateKeyword(message)) {
                /*
                    It is necessary to separate the message into the part without the date and only the date part because of the extractTimeInterval() method.
                    If we do not separate the message into these two parts, the date will be parsed as time. The TIME_PATTERN regex finds
                    whole numbers and the date consists of two, e.g. 09.10 will be parsed as today's date 9-10 am.
                 */
                String messageWithoutDate = message.substring(6);
                String datePartOfMessage = message.substring(0, 5);
                LocalDate date = LocalDate.parse(datePartOfMessage, DATE_FORMATTER);
                int days = (int)getNumberOfDaysBetweenTwoDates(DateTimeUtils.currentTime().toLocalDate(), date);
                return addDays(extractTimeInterval(messageWithoutDate), days);
            } else {
                return extractTimeInterval(message);
            }
        } else if (MessageType.DATE_INTERVAL.equals(messageType)) {
            return extractDateInterval(message);
        } else {
            throw new IllegalStateException("Message type not supported " + messageType);
        }
    }

}
