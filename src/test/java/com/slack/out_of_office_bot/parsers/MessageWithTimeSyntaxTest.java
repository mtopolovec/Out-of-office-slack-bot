package com.slack.out_of_office_bot.parsers;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.slack.out_of_office_bot.constants.RegexConstants.MESSAGE_WITH_TIME_REGEX_CHECK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageWithTimeSyntaxTest {

    @Test
    void checkMessageSyntaxForShortInputTime_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12 - 15";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTime_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12:30 - 15:45";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTime_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12 - 13:30";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForShortInputTimeWithKeywordTomorrow_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12 - 15";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTimeWithKeywordTomorrow_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12:33 - 15:26";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTimeWithKeywordTomorrow_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12 - 13:30";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForShortInputTimeWithDate_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12 - 13";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTimeWithDate_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12:33 - 15:26";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTimeWithDate_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12 - 13:30";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForShortInputTimeWithDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12 - 13 Lunch";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTimeWithDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12:45 - 13:15 Lunch";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTimeWithDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "12 - 12:30 Lunch";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForShortInputTimeWithKeywordTomorrowAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12 - 15 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTimeWithKeywordTomorrowAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12:35 - 15:45 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTimeWithKeywordTomorrowAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "tomorrow 12 - 14:30 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForShortInputTimeWithDateAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12 - 15 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForLongInputTimeWithDateAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12:35 - 15:45 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForMixShortAndLongInputTimeWithDateAndDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09 12 - 14:30 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxWhenDateKeywordEndWithDot_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.09. 12 - 14:30 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenMessageSyntaxNotContainsRequiredBlankSpaces() {
        String message = "12-15";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenMessageSyntaxContainsNotAllowedBlankSpaces() {
        String message = " 12:30 - 15:45";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntaxForLongTime_shouldReturnFalseWhenMessageContainsIncorrectTimeInput() {
        String message = "2:30 - 15:45 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenMessageContainsIncorrectTimeInput() {
        String message = "25.09 1245 - 15:45 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenMessageHasIncorrectSyntax() {
        String message = "25.09 1230 15 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenInputTimeContainsSec() {
        String message = "25.09 12:00:24 - 15:45:35 Doctor appointment";

        boolean check = Pattern.compile(MESSAGE_WITH_TIME_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }
}
