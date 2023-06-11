package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.parsers.TimeDateIntervalExtractor;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that does check's for OOORemoveCommandHandler
 */
@Service
public class RemoveCommandServiceImp implements RemoveCommandService {

    private final UserOOOInputRepository userOOOInputRepository;

    public RemoveCommandServiceImp(UserOOOInputRepository userOOOInputRepository) {
        this.userOOOInputRepository = userOOOInputRepository;
    }

    /**
     * Deletes user input from database only if users input dates match those from database
     * @param userId slack user id
     * @param message users message that has time interval that user want's to delete
     * @return deleted user input
     */
    @Override
    public Optional<UserOOOInput> deleteUserInput(String userId, String message) {
        DateTimeInterval dateTimeInterval = TimeDateIntervalExtractor.extractInterval(message);
        Optional<UserOOOInput> userOOOInput = userOOOInputRepository.findUserOOOInputByStartTimeAndEndTime(dateTimeInterval.getFrom(), dateTimeInterval.getTo());
        userOOOInput.ifPresent(oooInput -> userOOOInputRepository.deleteById(oooInput.getId()));
        return userOOOInput;
    }
}
