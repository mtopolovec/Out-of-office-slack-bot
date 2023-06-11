package com.slack.out_of_office_bot.handlers;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import org.springframework.stereotype.Service;

/**
 * Service that handles /hello command
 * Test method and nice addition to bot app with warm welcome
 */
@Service
public class HelloCommandHandler implements SlashCommandHandler{
    /**
     * To give a little life to bot and for him to be a little warmer
     * @param slashCommandRequest users request for hello command
     * @param context users context for hello command
     * @return String that makes and emoji wave and a nice welcome message Hello!
     */
    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) {
        return context.ack(":wave: Hello!");
    }
}
