package com.slack.out_of_office_bot.controller;

import com.slack.out_of_office_bot.dto.UserOOOInputDTO;
import com.slack.out_of_office_bot.service.UserOOOInputService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that exposes data for dashboard
 */

@CrossOrigin
@RestController
@RequestMapping("/api/out-of-office")
public class VacationController {
    private final UserOOOInputService userOOOInputService;

    public VacationController(UserOOOInputService userOOOInputService) {
        this.userOOOInputService = userOOOInputService;
    }

    @GetMapping
    public List<UserOOOInputDTO> allUserInputs() {
        return userOOOInputService.getAllUserOOOInputsDTO().get();
    }
}
