package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.slack.out_of_office_bot.constants.RegexConstants.DESCRIPTION_REGEX_CHECK;

/**
 * Service that does check's for OOOCommandHandler
 */
@Service
public class OOOCommandServiceImp implements OOOCommandService {

    private final UserOOOInputRepository userOOOInputRepository;

    public OOOCommandServiceImp(UserOOOInputRepository userOOOInputRepository) {
        this.userOOOInputRepository = userOOOInputRepository;
    }

    /**
     * Saves users input to database
     * @param userId slack user id e.g. U123456789
     * @param username users username e.g. jane.doe
     * @param description users description/reason why he is going out of office
     * @param dateTimeInterval interval between when user is going be out of office
     */
    @Override
    public void saveUserOOOInput(String userId, String username, String description, DateTimeInterval dateTimeInterval) {
        UserOOOInput userOOOInput = new UserOOOInput(null, userId, username, description, dateTimeInterval.getFrom(), dateTimeInterval.getTo());
        userOOOInputRepository.save(userOOOInput);
    }

    /**
     * Extracts description/reason from users message
     * @param message users message
     * @return description/reason from users message
     */
    @Override
    public String extractReason(String message) {
        Matcher matcher = Pattern.compile(DESCRIPTION_REGEX_CHECK).matcher(message);
        String description = null;
        if (matcher.find()) {
            description = matcher.group();
            description = description.substring(4);
        }
        return description;
    }

}
