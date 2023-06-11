package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.CheckAvailabilityResult;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import com.slack.out_of_office_bot.utility.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.slack.out_of_office_bot.constants.RegexConstants.USERID_REGEX_CHECK;
import static com.slack.out_of_office_bot.enums.AvailabilityStatus.*;

/**
 * Service that does check's for OOOCheckCommandHandler
 */
@Service
public class CheckCommandServiceImp implements CheckCommandService {

    private final UserOOOInputRepository userOOOInputRepository;

    public CheckCommandServiceImp(UserOOOInputRepository userOOOInputRepository) {
        this.userOOOInputRepository = userOOOInputRepository;
    }

    /**
     * Grabs the users input for today from database
     * @param userId Slack user id
     * @return CheckAvailabilityResult for asked slack user id from database
     */
    @Override
    public CheckAvailabilityResult checkUserInput(String userId) {
        CheckAvailabilityResult result = null;
        Optional<List<UserOOOInput>> userOOOInputs = userOOOInputRepository.findUserInputBetween(userId, DateTimeUtils.startOfDay(), DateTimeUtils.endOfDay());
        if (userOOOInputs.isEmpty() || userOOOInputs.get().isEmpty()) {
            result = new CheckAvailabilityResult(AVAILABLE_FOR_WHOLE_DAY);
            return result;
        }

        for (UserOOOInput u : userOOOInputs.get()) {
            if (u.isOneDay()) {
                if (u.hasPassed()) {
                    result = new CheckAvailabilityResult(CURRENTLY_AVAILABLE, u.getStartTime(), u.getEndTime());
                } else if (u.current()) {
                    result = new CheckAvailabilityResult(CURRENTLY_OUT_OF_OFFICE, u.getStartTime(), u.getEndTime());
                } else {
                    result = new CheckAvailabilityResult(GOING_OUT_OF_OFFICE, u.getStartTime(), u.getEndTime());
                }
            } else {
                result = new CheckAvailabilityResult(CURRENTLY_ON_VACATION, u.getStartTime(), u.getEndTime());
            }
        }
        return result;
    }

    /**
     * Grabs users slack id from his message e.g. /ooo-check @JaneDoe
     * - @JaneDoe is basicly an <U123456789|jane.doe> and we only need U123456789 part
     * @param commandArgText users command argument text e.g. @JaneDoe
     * @return users slack id extracted from users message e.g. U123456789
     */
    @Override
    public String getUserIdFromCommandArgumentText(String commandArgText) {
        Matcher matcher = Pattern.compile(USERID_REGEX_CHECK).matcher(commandArgText);
        String regexGrabbedUserId = null;
        if (matcher.find()) {
            regexGrabbedUserId = matcher.group();
        }
        return regexGrabbedUserId;
    }
}
