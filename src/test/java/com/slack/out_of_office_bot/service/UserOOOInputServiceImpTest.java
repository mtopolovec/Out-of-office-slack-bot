package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserOOOInputServiceImpTest {

    @Mock
    private UserOOOInputRepository userOOOInputRepository;
    @InjectMocks
    private UserOOOInputServiceImp messagesServiceImp;

    @Test
    void getAllUserOOOInputs_shouldReturnAllMessages() {
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);

        String slackUserId = "123456";
        String username = "testUser";
        String description = "testDescription";

        UserOOOInput userOOOInput = new UserOOOInput(1L, slackUserId, username, description, startDate, endDate);
        UserOOOInput userOOOInput1 = new UserOOOInput(2L, slackUserId, username, description, startDate, endDate);
        UserOOOInput userOOOInput2 = new UserOOOInput(3L, slackUserId, username, description, startDate, endDate);

        List<UserOOOInput> userOOOInputs = Arrays.asList(userOOOInput, userOOOInput1, userOOOInput2);

        when(userOOOInputRepository.findAll()).thenReturn(userOOOInputs);

        Optional<List<UserOOOInput>> actualUserOOOInputs = messagesServiceImp.getAllUserOOOInputs();

        assertThat(actualUserOOOInputs, is(notNullValue()));
        assertThat(actualUserOOOInputs.get().size(), equalTo(3));

    }

    @Test
    void getUserOOOInputById_shouldReturnMessageById() {
        final Long id = 1L;
        String slackUserId = "123456";
        String username = "testUser";
        String description = "testDescription";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);

        UserOOOInput userOOOInput = new UserOOOInput(id, slackUserId, username, description, startDate, endDate);

        when(userOOOInputRepository.findById(id)).thenReturn(Optional.of(userOOOInput));

        Optional<UserOOOInput> actualUserOOOInput = messagesServiceImp.getUserOOOInputById(id);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.get().getId(), equalTo(id));
        assertThat(actualUserOOOInput.get().getSlackUserId(), equalTo(slackUserId));
        assertThat(actualUserOOOInput.get().getSlackUsername(), equalTo(username));
        assertThat(actualUserOOOInput.get().getStartTime(), equalTo(startDate));
        assertThat(actualUserOOOInput.get().getEndTime(), equalTo(endDate));

    }

    @Test
    void addUserOOOInput_shouldAddMessageAndReturnTheAddedMessage() {
        final Long id = 1L;
        String slackUserId = "123456";
        String username = "testUser";
        String description = "testDescription";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);

        UserOOOInput userOOOInput = new UserOOOInput(id, slackUserId, username, description, startDate, endDate);

        when(userOOOInputRepository.save(userOOOInput)).thenReturn(userOOOInput);

        Optional<UserOOOInput> actualUserOOOInput = messagesServiceImp.addUserOOOInput(userOOOInput);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.get().getId(), equalTo(id));
        assertThat(actualUserOOOInput.get().getSlackUserId(), equalTo(slackUserId));
        assertThat(actualUserOOOInput.get().getSlackUsername(), equalTo(username));
        assertThat(actualUserOOOInput.get().getStartTime(), equalTo(startDate));
        assertThat(actualUserOOOInput.get().getEndTime(), equalTo(endDate));

    }

    @Test
    void updateUserOOOInput_shouldReturnUpdatedMessage() {
        final Long id = 1L;
        String slackUserId = "123456";
        String username = "testUser";
        String updatedUser = "wrongTestUser";
        String description = "testDescription";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);

        UserOOOInput userOOOInput = new UserOOOInput(id, slackUserId, username, description, startDate, endDate);
        when(userOOOInputRepository.save(userOOOInput)).thenReturn(userOOOInput);
        messagesServiceImp.addUserOOOInput(userOOOInput);

        UserOOOInput updatedUserOOOInput = new UserOOOInput(id, slackUserId, updatedUser, description, startDate, endDate);
        when(userOOOInputRepository.saveAndFlush(updatedUserOOOInput)).thenReturn(updatedUserOOOInput);

        Optional<UserOOOInput> actualUserOOOInput = messagesServiceImp.updateUserOOOInput(id, updatedUserOOOInput);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.get().getSlackUsername(), equalTo(updatedUser));
    }

    @Test
    void deleteUserOOOInput_shouldDeleteCorrectMessage() {
        final Long id = 1L;
        String slackUserId = "123456";
        String username = "testUser";
        String description = "testDescription";
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0);

        UserOOOInput userOOOInput = new UserOOOInput(id, slackUserId, username, description, startDate, endDate);

        when(userOOOInputRepository.findById(id)).thenReturn(Optional.of(userOOOInput));

        Optional<UserOOOInput> deletedUserInput = messagesServiceImp.deleteUserOOOInput(id);

        verify(userOOOInputRepository).deleteById(id);
        assertThat(deletedUserInput, is(notNullValue()));
        assertThat(deletedUserInput.get().getId(), equalTo(id));
        assertThat(deletedUserInput.get().getSlackUserId(), equalTo(slackUserId));
        assertThat(deletedUserInput.get().getSlackUsername(), equalTo(username));
        assertThat(deletedUserInput.get().getStartTime(), equalTo(startDate));
        assertThat(deletedUserInput.get().getEndTime(), equalTo(endDate));
    }
}