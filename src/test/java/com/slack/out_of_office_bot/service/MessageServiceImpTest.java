package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.slack.out_of_office_bot.constants.DateTimeFormatConstants.MESSAGE_DATE_FORMAT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImpTest {

    @InjectMocks
    private MessageServiceImp messageServiceImp;

    @Test
    void userOutOfOfficeMessage_shouldReturnTimeFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        String finalMsgWithDescription = "<@" + userId + ">" +
                "\nIs out of office for '" + description + "' from " + startHour + ":00 until " + endHour + ":00.";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                "\nIs out of office from " + startHour + ":00 until " + endHour + ":00.";
        LocalDateTime startDate = DateTimeUtils.currentTime().withHour(startHour).withMinute(0);
        LocalDateTime endDate = DateTimeUtils.currentTime().withHour(endHour).withMinute(0);

        String msgWithDescription = messageServiceImp.userOutOfOfficeMessage(userId, startDate, endDate, description);
        String noDescriptionMsg = messageServiceImp.userOutOfOfficeMessage(userId, startDate, endDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void userOutOfOfficeMessage_shouldReturnDateFormat() {
        String userId = "123456";
        String description = "Vacation";
        int startDay = 30;
        int endDay = 31;
        int month = 12;
        String finalMsgWithDescription = "<@" + userId + ">" +
                "\nIs out of office for '" + description + "' from " + startDay + "." + month + " until " + endDay + "." + month + ".";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                "\nIs out of office from " + startDay + "." + month + " until " + endDay + "." + month + ".";
        LocalDateTime inputStartDate = DateTimeUtils.startOfDay().withMonth(month).withDayOfMonth(startDay);
        LocalDateTime inputEndDate = DateTimeUtils.endOfDay().withMonth(month).withDayOfMonth(endDay);

        String msgWithDescription = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void userOutOfOfficeMessage_shouldReturnKeywordTomorrowFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        int days = 1;
        LocalDateTime inputStartDate = DateTimeUtils.currentTime().plusDays(days).withHour(startHour).withMinute(0);
        LocalDateTime inputEndDate = DateTimeUtils.currentTime().plusDays(days).withHour(endHour).withMinute(0);

        String finalMsgWithDescription = "<@" + userId + ">" +
                "\nIs out of office for '" + description + "' tomorrow from " + startHour + ":00 until " + endHour + ":00.";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                "\nIs out of office tomorrow from " + startHour + ":00 until " + endHour + ":00.";

        String msgWithDescription = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void userOutOfOfficeMessage_shouldReturnWithDateKeywordFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        int days = 3;
        LocalDateTime inputStartDate = DateTimeUtils.currentTime().plusDays(days).withHour(startHour).withMinute(0);
        LocalDateTime inputEndDate = DateTimeUtils.currentTime().plusDays(days).withHour(endHour).withMinute(0);

        String finalMsgWithDescription = "<@" + userId + ">" +
                "\nIs out of office for '" + description + "' " + inputStartDate.format(MESSAGE_DATE_FORMAT) + " from " + startHour + ":00 until " + endHour + ":00.";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                "\nIs out of office " + inputStartDate.format(MESSAGE_DATE_FORMAT) + " from " + startHour + ":00 until " + endHour + ":00.";

        String msgWithDescription = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void userOutOfOfficeMessage_shouldReturnShortDateFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startDay = 31;
        int endDay = 31;
        int month = 12;
        LocalDateTime inputStartDate = DateTimeUtils.startOfDay().withMonth(month).withDayOfMonth(startDay);
        LocalDateTime inputEndDate = DateTimeUtils.endOfDay().withMonth(month).withDayOfMonth(endDay);

        String finalMsgWithDescription = "<@" + userId + ">" +
                "\nIs out of office for '" + description + "' " + startDay + "." + month + ".";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                "\nIs out of office " + startDay + "." + month + ".";

        String msgWithDescription = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.userOutOfOfficeMessage(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));
    }


    @Test
    void msgUserInputSuccessfullyDeleted_shouldReturnTimeFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        String finalMsgWithDescription = "<@" + userId + ">" +
                " removed '" + description + "' from " + startHour + ":00 until " + endHour + ":00.";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                " removed from " + startHour + ":00 until " + endHour + ":00.";
        LocalDateTime startDate = DateTimeUtils.currentTime().withHour(startHour).withMinute(0);
        LocalDateTime endDate = DateTimeUtils.currentTime().withHour(endHour).withMinute(0);

        String msgWithDescription = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, startDate, endDate, description);
        String noDescriptionMsg = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, startDate, endDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void msgUserInputSuccessfullyDeleted_shouldReturnDateFormat() {
        String userId = "123456";
        String description = "Vacation";
        int startDay = 10;
        int endDay = 24;
        int month = 10;

        String finalMsgWithDescription = "<@" + userId + ">" +
                " removed '" + description + "' from " + startDay + "." + month + " until " + endDay + "." + month + ".";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                " removed from " + startDay + "." + month + " until " + endDay + "." + month + ".";

        LocalDateTime inputStartDate = DateTimeUtils.startOfDay().withMonth(month).withDayOfMonth(startDay);
        LocalDateTime inputEndDate = DateTimeUtils.endOfDay().withMonth(month).withDayOfMonth(endDay);

        String msgWithDescription = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }

    @Test
    void msgUserInputSuccessfullyDeleted_shouldReturnKeywordTomorrowFormat() {
        String userId = "123456";
        String description = "Doctor appointment";
        int startHour = 12;
        int endHour = 15;
        int days = 1;
        LocalDateTime inputStartDate = DateTimeUtils.currentTime().plusDays(days).withHour(startHour).withMinute(0);
        LocalDateTime inputEndDate = DateTimeUtils.currentTime().plusDays(days).withHour(endHour).withMinute(0);

        String finalMsgWithDescription = "<@" + userId + ">" +
                " removed '" + description + "' from tomorrow " + startHour + ":00 until " + endHour + ":00.";
        String finalNoDescriptionMsg = "<@" + userId + ">" +
                " removed from tomorrow " + startHour + ":00 until " + endHour + ":00.";

        String msgWithDescription = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, inputStartDate, inputEndDate, description);
        String noDescriptionMsg = messageServiceImp.msgUserInputSuccessfullyDeleted(userId, inputStartDate, inputEndDate, null);

        assertThat(msgWithDescription, is(notNullValue()));
        assertThat(noDescriptionMsg, is(notNullValue()));
        assertThat(msgWithDescription, equalTo(finalMsgWithDescription));
        assertThat(noDescriptionMsg, equalTo(finalNoDescriptionMsg));

    }
}