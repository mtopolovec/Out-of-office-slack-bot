package com.slack.out_of_office_bot.handlers;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.out_of_office_bot.model.CheckAvailabilityResult;
import com.slack.out_of_office_bot.service.CheckCommandService;
import com.slack.out_of_office_bot.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * Service that handles /ooo-check command e.g. /ooo-check @JaneDoe
 * Checks if user is available at the time of check
 */
@Service
public class OOOCheckCommandHandler implements SlashCommandHandler {

    private final CheckCommandService checkCommandService;
    private final MessageService messageService;

    public OOOCheckCommandHandler(CheckCommandService checkCommandService, MessageService messageService) {
        this.checkCommandService = checkCommandService;
        this.messageService = messageService;
    }

    /**
     * Method that makes a response message that bot will post for user when he checks if someone is out of office
     * @param slashCommandRequest users request from /ooo-check command
     * @param context user context for /ooo-check command
     * @return user message that bot posts to user only
     */
    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) {
        String commandArgText = slashCommandRequest.getPayload().getText();
        if (commandArgText == null) {
            return context.ack(messageService.checkCommandErrorMessage());
        }
        String userId = checkCommandService.getUserIdFromCommandArgumentText(commandArgText);
        if (userId == null) {
            return context.ack(messageService.checkCommandErrorMessage());
        }
        CheckAvailabilityResult userOOOInput = checkCommandService.checkUserInput(userId);

        return switch (userOOOInput.getAvailabilityStatus()) {
            case AVAILABLE_FOR_WHOLE_DAY -> context.ack(messageService.msgUserIsAvailableForWholeDay(userId));
            case CURRENTLY_AVAILABLE -> context.ack(messageService.msgUserIsCurrentlyAvailable(userId));
            case CURRENTLY_OUT_OF_OFFICE -> context.ack(messageService.msgUserIsCurrentlyOutOfOffice(userId, userOOOInput.getEnd()));
            case GOING_OUT_OF_OFFICE -> context.ack(messageService.msgUserIsOutFromTimeToTime(userId, userOOOInput.getStart(), userOOOInput.getEnd()));
            case CURRENTLY_ON_VACATION -> context.ack(messageService.msgUserIsOutWholeDay(userId));
            default -> throw new IllegalStateException("Invalid availability status: " + userOOOInput.getAvailabilityStatus());
        };
    }
}
