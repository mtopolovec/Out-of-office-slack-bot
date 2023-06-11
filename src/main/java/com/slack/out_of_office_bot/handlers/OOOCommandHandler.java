package com.slack.out_of_office_bot.handlers;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.out_of_office_bot.model.DateTimeInterval;
import com.slack.out_of_office_bot.parsers.TimeDateIntervalExtractor;
import com.slack.out_of_office_bot.service.MessageService;
import com.slack.out_of_office_bot.service.OOOCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;

/**
 * Service that handles /ooo command e.g. /ooo 12:00 - 13:00 Lunch
 * Adds that user is going to be out of office
 */
@Slf4j
@Service
public class OOOCommandHandler implements SlashCommandHandler {

    private final OOOCommandService oooCommandService;
    private final MessageService messageService;

    public OOOCommandHandler(OOOCommandService oooCommandService, MessageService messageService) {
        this.oooCommandService = oooCommandService;
        this.messageService = messageService;
    }

    /**
     * Method that makes a response message that bot will post on channel that user is out of office
     * @param slashCommandRequest users request from /ooo command
     * @param context user context for /ooo command
     * @return user out of office message for bot to post on channel for everyone to see
     */
    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) {
        String commandArgText = slashCommandRequest.getPayload().getText();
        String userId = slashCommandRequest.getPayload().getUserId();
        String username = slashCommandRequest.getPayload().getUserName();
        try {
            DateTimeInterval dateTimeInterval = TimeDateIntervalExtractor.extractInterval(commandArgText);
            String reason = oooCommandService.extractReason(commandArgText);

            oooCommandService.saveUserOOOInput(userId, username, reason, dateTimeInterval);

            // Post user out of office message to channel
            context.client().chatPostMessage(r -> r.token(context.getBotToken())
                    .channel(slashCommandRequest.getPayload().getChannelId())
                    .text(messageService.userOutOfOfficeMessage(userId, dateTimeInterval.getFrom(), dateTimeInterval.getTo(), reason)));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            if (ex instanceof DateTimeException) {
                return context.ack(messageService.futureDateErrorMessage());
            } else {
                return context.ack(messageService.getSubmitErrorMessage());
            }
        }
        return context.ack();
    }


}
