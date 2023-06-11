package com.slack.out_of_office_bot.constants;

import java.util.regex.Pattern;

/**
 * Regex patterns for regex check of user inputs
 */
public class RegexConstants {
    /**
     * Regex pattern for correct message with time input e.g. tomorrow 12 - 13 Lunch, 12:00 - 13:00 Lunch, tomorrow 12 - 13:30 Lunch, 25.09 12 - 15 Doctor appointment
     */
    public static  final String MESSAGE_WITH_TIME_REGEX_CHECK = "^(tomorrow |((0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])(|[.])) |)((([01][0-9]|2[0-3]):([0-5][0-9]))|([0-1][0-9]|2[0-3])) - ((([01][0-9]|2[0-3]):([0-5][0-9]))|([0-1][0-9]|2[0-3]))($| .*)";
    /**
     * Regex pattern for time e.g. 12:00 or 12
     */
    public static  final Pattern TIME_PATTERN = Pattern.compile("(([01][0-9]|2[0-3]):([0-5][0-9]))|([0-1][0-9]|2[0-3])");
    /**
     * Regex pattern for correct message with date input e.g. 25.10 Lunch, 25.10 - 26.10 Lunch
     */
    public static final String MESSAGE_WITH_DATE_REGEX_CHECK = "^((0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012]))([.]|)(| - (0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])([.]|))($| [a-žA-Ž1-9 ]+$)";
    /**
     * Regex pattern for date e.g. 10.08
     */
    public static  final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])");
    /**
     * Regex pattern for users reason e.g. Lunch break, Vacation
     */
    public static  final String DESCRIPTION_REGEX_CHECK = ":[0-9]+\\s([a-žA-ž]+( [a-žA-Ž]+)+)|.(0?[1-9]|[1][0-2])\\s([a-žA-Ž]+( [a-žA-Ž]+)+)|:[0-9]+\\s[a-žA-Ž]+|.(0?[1-9]|[1][0-2])\\s[a-žA-Ž]+|(- [1-9])\\s[a-žA-Ž]+|.([0-5][0-9])\\s[a-žA-Ž]+";
    /**
     * Regex pattern for slack user id e.g. U123456789
     */
    public static  final String USERID_REGEX_CHECK = "U([0-9]+([a-zA-Z]+[0-9]+)+)";
}
