package com.slack.out_of_office_bot.converter;

import com.slack.out_of_office_bot.dto.UserOOOInputDTO;
import com.slack.out_of_office_bot.model.UserOOOInput;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class UserOOOInputToUserOOOInputDTO implements Converter<UserOOOInput, UserOOOInputDTO> {
    @Override
    public UserOOOInputDTO convert(UserOOOInput source) {
        if(source == null) {
            return null;
        }

        return new UserOOOInputDTO(
                source.getId(),
                source.getSlackUserId(),
                createCorrectNameAndSurname(source.getSlackUsername()),
                source.getDescription(),
                source.getStartTime(),
                source.getEndTime().minusMinutes(1)
        );
    }

    private String createCorrectNameAndSurname(String username) {
        if(username == null) {
            return null;
        }
        String[] grabFirstNameAndLast = (username.split("\\."));
        return Arrays.stream(grabFirstNameAndLast).map(s -> s.substring(0,1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }
}
