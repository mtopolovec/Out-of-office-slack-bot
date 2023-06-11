package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OOOCommandServiceImpTest {

    @Mock
    private UserOOOInputRepository userOOOInputRepository;
    @InjectMocks
    private OOOCommandServiceImp oooCommandServiceImp;

    @Test
    void saveUserOOOInput_shouldSaveUserOOOInputToDatabase() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        Long id = 1L;
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = DateTimeUtils.currentTime().withHour(startHour).withMinute(0);
        LocalDateTime endDate = DateTimeUtils.currentTime().withHour(endHour).withMinute(0);
        DateTimeInterval interval = new DateTimeInterval(startDate, endDate);
        UserOOOInput userOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);


        when(userOOOInputRepository.getById(id)).thenReturn(userOOOInput);

        oooCommandServiceImp.saveUserOOOInput(userId, username, description, interval);
        UserOOOInput userOOOInputReceived = userOOOInputRepository.getById(id);

        assertThat(userOOOInputReceived, is(notNullValue()));
        assertThat(userOOOInputReceived.getId(), equalTo(id));
        assertThat(userOOOInputReceived.getSlackUserId(), equalTo(userId));
        assertThat(userOOOInputReceived.getSlackUsername(), equalTo(username));
        assertThat(userOOOInputReceived.getDescription(), equalTo(description));
        assertThat(userOOOInputReceived.getStartTime(), equalTo(startDate));
        assertThat(userOOOInputReceived.getEndTime(), equalTo(endDate));
    }

    @Test
    void extractReason_shouldReturnReasonOnly() {
        // All examples to check regex working for all these examples
        String timeMessageWithMoreWords = "12:00 - 13:00 need to pick up car";
        String timeMessageSingleWord = "12:00 - 13:00 Lunch";
        String dateMessageWithMoreWords = "10.08 - 25.08 Vacation on the sea";
        String dateMessageSingleWord = "10.08 - 25.08 Vacation";
        String croatianMessageDescription = "12:00 - 13:00 Idem na čevape";

        String descriptionTimeMessageWithMoreWords = "need to pick up car";
        String descriptionTimeMessageSingleWord = "Lunch";
        String descriptionDateMessageWithMoreWords = "Vacation on the sea";
        String descriptionDateMessageSingleWord = "Vacation";
        String descriptionCroatianMessage = "Idem na čevape";

        String actualDescription = oooCommandServiceImp.extractReason(timeMessageWithMoreWords);
        assertThat(actualDescription, is(notNullValue()));
        assertThat(actualDescription, equalTo(descriptionTimeMessageWithMoreWords));

        actualDescription = oooCommandServiceImp.extractReason(timeMessageSingleWord);
        assertThat(actualDescription, is(notNullValue()));
        assertThat(actualDescription, equalTo(descriptionTimeMessageSingleWord));

        actualDescription = oooCommandServiceImp.extractReason(dateMessageWithMoreWords);
        assertThat(actualDescription, is(notNullValue()));
        assertThat(actualDescription, equalTo(descriptionDateMessageWithMoreWords));

        actualDescription = oooCommandServiceImp.extractReason(dateMessageSingleWord);
        assertThat(actualDescription, is(notNullValue()));
        assertThat(actualDescription, equalTo(descriptionDateMessageSingleWord));

        actualDescription = oooCommandServiceImp.extractReason(croatianMessageDescription);
        assertThat(actualDescription, is(notNullValue()));
        assertThat(actualDescription, equalTo(descriptionCroatianMessage));
    }

    @Test
    void extractReason_shouldReturnNullIfNoReasonProvided() {
        String message = "12:00 - 13:00";

        String description = oooCommandServiceImp.extractReason(message);
        assertThat(description, equalTo(null));
    }
}