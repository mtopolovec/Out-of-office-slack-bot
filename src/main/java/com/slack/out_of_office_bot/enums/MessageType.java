package com.slack.out_of_office_bot.enums;

import com.slack.out_of_office_bot.constants.RegexConstants;

import java.util.regex.Pattern;

/**
 * Types of supported message formats with respective pattern formats.
 */
public enum MessageType {
    /**
     * Representing a message with time interval e.g. '12:00 - 14:00 Out for lunch'
     */
    TIME_INTERVAL(RegexConstants.TIME_PATTERN),
    /**
     * Representing a message with date interval e.g. '12.12 - 16.12 Out for vacation'
     */
    DATE_INTERVAL(RegexConstants.DATE_PATTERN);

    private final Pattern pattern;

    MessageType(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
