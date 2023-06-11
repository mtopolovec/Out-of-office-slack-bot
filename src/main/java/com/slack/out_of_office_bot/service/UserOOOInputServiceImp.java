package com.slack.out_of_office_bot.service;

import com.slack.out_of_office_bot.converter.UserOOOInputToUserOOOInputDTO;
import com.slack.out_of_office_bot.dto.UserOOOInputDTO;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.repository.UserOOOInputRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service used for CRUD (Create, Read, Update and Delete) operations on database
 * Intended for future upgrades if someday we gona make endpoints and expose them
 * on frontend and use them to add Out of office messages and only check them with bot!!!!
 */
@Service
public class UserOOOInputServiceImp implements UserOOOInputService {

    private final UserOOOInputRepository userOOOInputRepository;
    private final UserOOOInputToUserOOOInputDTO userOOOInputToUserOOOInputDTO;

    public UserOOOInputServiceImp(UserOOOInputRepository userOOOInputRepository, UserOOOInputToUserOOOInputDTO userOOOInputToUserOOOInputDTO) {
        this.userOOOInputRepository = userOOOInputRepository;
        this.userOOOInputToUserOOOInputDTO = userOOOInputToUserOOOInputDTO;
    }

    /**
     * Method that grabs all users inputs from database
     * @return all users inputs from database
     */
    @Override
    public Optional<List<UserOOOInput>> getAllUserOOOInputs() {
        return Optional.of(userOOOInputRepository.findAll());
    }

    /**
     * Method that grabs all users inputs from database
     * @return all users inputs from database
     */
    @Override
    public Optional<List<UserOOOInputDTO>> getAllUserOOOInputsDTO() {
        return Optional.of(userOOOInputRepository.findAll()
                .stream()
                .map(userOOOInputToUserOOOInputDTO::convert)
                .collect(Collectors.toList()));
    }

    /**
     * Method that grabs only one user input by id from database
     * @param id database id number in order to grab users input by id
     * @return users input by id
     */
    @Override
    public Optional<UserOOOInput> getUserOOOInputById(Long id) {
        return userOOOInputRepository.findById(id);
    }

    /**
     * Method to add new user input to database
     * @param userOOOInput new user input values to store to database
     * @return stored user input
     */
    @Override
    public Optional<UserOOOInput> addUserOOOInput(UserOOOInput userOOOInput) {
        return Optional.of(userOOOInputRepository.save(userOOOInput));
    }

    /**
     * Method to update user input to database
     * @param id database id number in order to update the correct user input
     * @param userOOOInput new user input values that need to be updated to database
     * @return updated user input
     */
    @Override
    public Optional<UserOOOInput> updateUserOOOInput(Long id, UserOOOInput userOOOInput) {
        userOOOInputRepository.saveAndFlush(userOOOInput);
        return Optional.of(userOOOInput);
    }

    /**
     * Method to delete user input from database
     * @param id database id number in order to delete the correct user input
     */
    @Override
    public Optional<UserOOOInput> deleteUserOOOInput(Long id) {
        Optional<UserOOOInput> deletedUserOOOInput = userOOOInputRepository.findById(id);
        userOOOInputRepository.deleteById(id);
        return deletedUserOOOInput;
    }
}
