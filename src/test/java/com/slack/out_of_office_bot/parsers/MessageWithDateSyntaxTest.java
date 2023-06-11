package com.slack.out_of_office_bot.parsers;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.slack.out_of_office_bot.constants.RegexConstants.MESSAGE_WITH_DATE_REGEX_CHECK;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MessageWithDateSyntaxTest {

    @Test
    void checkMessageSyntaxForWholeDay_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.10";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForWholeDayWithDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.10. Vacation";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForInputDate_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.10 - 30.10.";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForInputDateWithDescription_shouldReturnTrueWhenMessageSyntaxIsCorrect() {
        String message = "25.10. - 30.10 Vacation";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertTrue(check);
    }

    @Test
    void checkMessageSyntaxForInputDateWithDescription_shouldReturnFalseWhenMessageSyntaxIsIncorrect() {
        String message = "25.10 - 30.120 Vacation";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }

    @Test
    void checkMessageSyntax_shouldReturnFalseWhenMessageSyntaxIsIncorrect() {
        String message = "10.12 Vacation - 12.12Vacation";

        boolean check = Pattern.compile(MESSAGE_WITH_DATE_REGEX_CHECK).matcher(message).find();

        assertFalse(check);
    }



}
