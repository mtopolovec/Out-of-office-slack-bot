package com.slack.out_of_office_bot.converter;

import com.slack.out_of_office_bot.dto.UserOOOInputDTO;
import com.slack.out_of_office_bot.model.UserOOOInput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class UserOOOInputDTOToUserOOOInput implements Converter<UserOOOInputDTO, UserOOOInput> {
    @Override
    public UserOOOInput convert(UserOOOInputDTO source) {
        if(source == null) {
            return null;
        }

        final UserOOOInput userOOOInput = new UserOOOInput();
        userOOOInput.setId(source.getId());
        userOOOInput.setSlackUserId(source.getSlackUserId());
        userOOOInput.setSlackUsername(createCorrectNameAndSurname(source.getName()));
        userOOOInput.setDescription(source.getDescription());
        userOOOInput.setStartTime(source.getStart());
        userOOOInput.setEndTime(source.getEnd());

        return userOOOInput;
    }

    private String createCorrectNameAndSurname(String username) {
        if(username == null) {
            return null;
        }
        String[] grabFirstNameAndLast = (username.split(" "));
        return Arrays.stream(grabFirstNameAndLast).map(String::toLowerCase)
                .collect(Collectors.joining("."));
    }
}
