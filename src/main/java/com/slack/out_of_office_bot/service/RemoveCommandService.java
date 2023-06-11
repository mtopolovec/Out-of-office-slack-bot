package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.UserOOOInput;

import java.util.Optional;

public interface RemoveCommandService {
    Optional<UserOOOInput> deleteUserInput(String userId, String message);
}
