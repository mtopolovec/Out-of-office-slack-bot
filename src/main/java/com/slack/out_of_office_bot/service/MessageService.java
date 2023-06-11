package com.slack.out_of_office_bot.service;

import java.time.LocalDateTime;

public interface MessageService {
    String getSubmitErrorMessage();
    String futureDateErrorMessage();
    String checkCommandErrorMessage();
    String removeCommandErrorMessage();
    String userNotFound(String userId);
    String userOutOfOfficeMessage(String userId, LocalDateTime startDate, LocalDateTime endDate, String description);
    String msgUserIsAvailableForWholeDay(String userId);
    String msgUserIsCurrentlyAvailable(String userId);
    String msgUserIsCurrentlyOutOfOffice(String userId, LocalDateTime endTime);
    String msgUserIsOutFromTimeToTime(String userId, LocalDateTime startTime, LocalDateTime endTime);
    String msgUserIsOutWholeDay(String userId);
    String msgUserInputSuccessfullyDeleted(String userId, LocalDateTime startTime, LocalDateTime endTime, String description);
    String msgNoInputsForToday(String userId, LocalDateTime startTime, LocalDateTime endTime);
}
