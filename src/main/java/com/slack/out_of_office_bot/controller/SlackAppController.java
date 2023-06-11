package com.slack.out_of_office_bot.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;

import javax.servlet.annotation.WebServlet;

/**
 * Main slack controller that exposes endpoint for slack commands to work
 */
@WebServlet("/slack/events")
public class SlackAppController extends SlackAppServlet {
    public SlackAppController(App app) {
        super(app);
    }
}
