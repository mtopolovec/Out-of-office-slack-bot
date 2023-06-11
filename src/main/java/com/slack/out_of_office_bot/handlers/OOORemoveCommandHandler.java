package com.slack.out_of_office_bot.handlers;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.model.UserOOOInput;
import com.slack.out_of_office_bot.parsers.TimeDateIntervalExtractor;
import com.slack.out_of_office_bot.service.MessageService;
import com.slack.out_of_office_bot.service.RemoveCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that handles /ooo-remove command e.g. /ooo-remove 12:00 - 13:00
 * Removes users out of office input
 */
@Slf4j
@Service
public class OOORemoveCommandHandler implements SlashCommandHandler {

    private final MessageService messageService;
    private final RemoveCommandService removeCommandService;

    public OOORemoveCommandHandler(MessageService messageService, RemoveCommandService removeCommandService) {
        this.messageService = messageService;
        this.removeCommandService = removeCommandService;
    }

    /**
     * Method that makes a response message user has removed his input and that message will be posted on channel by bot
     * @param slashCommandRequest users request from /ooo-remove command
     * @param context user context for /ooo-remove command
     * @return that user has deleted his out of office message so that bot can post it on channel for everyone to see
     */
    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) {
        String commandArgText = slashCommandRequest.getPayload().getText();
        if (commandArgText == null) {
            return context.ack(messageService.removeCommandErrorMessage());
        }
        String userId = slashCommandRequest.getPayload().getUserId();
        Optional<UserOOOInput> userOOOInput = removeCommandService.deleteUserInput(userId, commandArgText);

        if (userOOOInput.isEmpty()) {
            DateTimeInterval dateTimeInterval = TimeDateIntervalExtractor.extractInterval(commandArgText);
            return context.ack(messageService.msgNoInputsForToday(userId, dateTimeInterval.getFrom(), dateTimeInterval.getTo()));
        }
        try {
            context.client().chatPostMessage(r -> r.token(context.getBotToken())
                    .channel(slashCommandRequest.getPayload().getChannelId())
                    .text(messageService.msgUserInputSuccessfullyDeleted(userId, userOOOInput.get().getStartTime(), userOOOInput.get().getEndTime(), userOOOInput.get().getDescription())));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return context.ack(messageService.removeCommandErrorMessage());
        }
        return context.ack();
    }
}
