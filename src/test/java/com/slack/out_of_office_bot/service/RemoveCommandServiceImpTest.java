package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveCommandServiceImpTest {

    @Mock
    private UserOOOInputRepository userOOOInputRepository;
    @InjectMocks
    private RemoveCommandServiceImp removeCommandServiceImp;

    @Test
    void deleteUserInput_shouldReturnDeletedUserInput() {
        String userId = "123456";
        String username = "jane.doe";
        String description = "Doctor appointment";
        String deleteMessageFromUserInput = "12:00 - 15:00";
        Long id = 1L;
        int startHour = 12;
        int endHour = 15;
        LocalDateTime startDate = LocalDateTime.now().withHour(startHour).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(endHour).withMinute(0).withSecond(0).withNano(0);
        UserOOOInput userOOOInput = new UserOOOInput(id, userId, username, description, startDate, endDate);


        when(userOOOInputRepository.findUserOOOInputByStartTimeAndEndTime(startDate, endDate)).thenReturn(Optional.of(userOOOInput));

        Optional<UserOOOInput> actualUserOOOInput = removeCommandServiceImp.deleteUserInput(userId, deleteMessageFromUserInput);

        assertThat(actualUserOOOInput, is(notNullValue()));
        assertThat(actualUserOOOInput.get().getId(), equalTo(id));
        assertThat(actualUserOOOInput.get().getSlackUserId(), equalTo(userId));
        assertThat(actualUserOOOInput.get().getSlackUsername(), equalTo(username));
        assertThat(actualUserOOOInput.get().getStartTime(), equalTo(startDate));
        assertThat(actualUserOOOInput.get().getEndTime(), equalTo(endDate));

        verify(userOOOInputRepository).deleteById(id);
    }
}