package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.CheckAvailabilityResult;

public interface CheckCommandService {
    CheckAvailabilityResult checkUserInput(String user);
    String getUserIdFromCommandArgumentText(String commandArgText);
}
