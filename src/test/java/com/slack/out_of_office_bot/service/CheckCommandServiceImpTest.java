package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.CheckAvailabilityResult;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.slack.out_of_office_bot.enums.AvailabilityStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckCommandServiceImpTest {

    @Mock
    private UserOOOInputRepository userOOOInputRepository;
    @InjectMocks
    private CheckCommandServiceImp checkCommandServiceImp;

    @Test
    void checkUserInput_shouldReturnAvailableForWholeDay() {
        String userId = "123456";
        LocalDateTime testStartDate = DateTimeUtils.startOfDay();
        LocalDateTime testEndDate = DateTimeUtils.endOfDay();

        when(userOOOInputRepository.findUserInputBetween(userId, testStartDate, testEndDate)).thenReturn(Optional.empty());

        CheckAvailabilityResult actualUserOOOInput = checkCommandServiceImp.checkUserInput(userId);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.getAvailabilityStatus(), equalTo(AVAILABLE_FOR_WHOLE_DAY));
    }

    @Test
    void checkUserInput_shouldReturnCurrentlyAvailable() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        Long id = 1L;
        int firstStartHour = 2;
        int firstEndHour = 3;
        int secondStartHour = 5;
        int secondEndHour = 6;
        int theDayBefore = 1;
        LocalDateTime startDate = LocalDateTime.now().withHour(firstStartHour).withMinute(0).minusDays(theDayBefore);
        LocalDateTime endDate = LocalDateTime.now().withHour(firstEndHour).withMinute(0).minusDays(theDayBefore);
        LocalDateTime testStartDate = DateTimeUtils.startOfDay();
        LocalDateTime testEndDate = DateTimeUtils.endOfDay();
        UserOOOInput userOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);
        UserOOOInput secondUserOOOInput = new UserOOOInput(id, userId, username, description, startDate.withHour(secondStartHour), endDate.withHour(secondEndHour));

        when(userOOOInputRepository.findUserInputBetween(userId, testStartDate, testEndDate)).thenReturn(Optional.of(Arrays.asList(userOOOInput, secondUserOOOInput)));

        CheckAvailabilityResult actualUserOOOInput = checkCommandServiceImp.checkUserInput(userId);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.getAvailabilityStatus(), equalTo(CURRENTLY_AVAILABLE));
    }

    @Test
    void checkUserInput_shouldReturnCurrentlyOutOfOffice() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        Long id = 1L;
        int hour = 1;
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(hour);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(hour);
        LocalDateTime testStartDate = DateTimeUtils.startOfDay();
        LocalDateTime testEndDate = DateTimeUtils.endOfDay();
        UserOOOInput userOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);

        when(userOOOInputRepository.findUserInputBetween(userId, testStartDate, testEndDate)).thenReturn(Optional.of(Arrays.asList(userOOOInput)));

        CheckAvailabilityResult actualUserOOOInput = checkCommandServiceImp.checkUserInput(userId);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.getAvailabilityStatus(), equalTo(CURRENTLY_OUT_OF_OFFICE));
    }

    @Test
    void checkUserInput_shouldReturnGoingOutOfOffice() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        Long id = 1L;
        int startHour = 1;
        int endHour = 2;
        LocalDateTime startDate = LocalDateTime.now().plusHours(startHour);
        LocalDateTime endDate = LocalDateTime.now().plusHours(endHour);
        LocalDateTime testStartDate = DateTimeUtils.startOfDay();
        LocalDateTime testEndDate = DateTimeUtils.endOfDay();
        UserOOOInput userOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);

        when(userOOOInputRepository.findUserInputBetween(userId, testStartDate, testEndDate)).thenReturn(Optional.of(Arrays.asList(userOOOInput)));

        CheckAvailabilityResult actualUserOOOInput = checkCommandServiceImp.checkUserInput(userId);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.getAvailabilityStatus(), equalTo(GOING_OUT_OF_OFFICE));
    }

    @Test
    void checkUserInput_shouldReturnCurrentlyOnVacation() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        Long id = 1L;
        int startHour = 8;
        int endHour = 10;
        int extraDays = 5;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0).minusDays(extraDays);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0).plusDays(extraDays);
        LocalDateTime testStartDate = DateTimeUtils.startOfDay();
        LocalDateTime testEndDate = DateTimeUtils.endOfDay();
        UserOOOInput vacationUserOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);

        when(userOOOInputRepository.findUserInputBetween(userId, testStartDate, testEndDate)).thenReturn(Optional.of(Arrays.asList(vacationUserOOOInput)));

        CheckAvailabilityResult actualUserOOOInput = checkCommandServiceImp.checkUserInput(userId);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.getAvailabilityStatus(), equalTo(CURRENTLY_ON_VACATION));
    }

    @Test
    void getUserIdFromCommandArgumentText_shouldReturnSlackUserId() {
        String userId = "U0123ABCDE4";
        String commandArgumentText = "<@U0123ABCDE4|jane.doe>";

        String actualUserId = checkCommandServiceImp.getUserIdFromCommandArgumentText(commandArgumentText);

        assertThat(actualUserId, is(notNullValue()));
        assertThat(actualUserId, equalTo(userId));
    }

    @Test
    void getUserIdFromCommandArgumentText_shouldReturnNullIfWrongStringSent() {
        String commandArgumentText = "jane.doe";

        String actualUserId = checkCommandServiceImp.getUserIdFromCommandArgumentText(commandArgumentText);

        assertThat(actualUserId, equalTo(null));
    }
}