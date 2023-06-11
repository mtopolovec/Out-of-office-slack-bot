package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.dto.UserOOOInputDTO;
import com.slack.out_of_office_bot.model.UserOOOInput;

import java.util.List;
import java.util.Optional;

public interface
UserOOOInputService {
    Optional<List<UserOOOInput>> getAllUserOOOInputs();
    Optional<List<UserOOOInputDTO>> getAllUserOOOInputsDTO();
    Optional<UserOOOInput> getUserOOOInputById(Long id);
    Optional<UserOOOInput> addUserOOOInput(UserOOOInput userOOOInput);
    Optional<UserOOOInput> updateUserOOOInput(Long id, UserOOOInput userOOOInput);
    Optional<UserOOOInput> deleteUserOOOInput(Long id);
}
