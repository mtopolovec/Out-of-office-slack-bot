package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.DateTimeInterval;

public interface OOOCommandService {
    void saveUserOOOInput(String userId, String username, String description, DateTimeInterval dateTimeInterval);
    String extractReason(String message);
}
