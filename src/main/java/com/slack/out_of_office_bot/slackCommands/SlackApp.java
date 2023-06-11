package com.slack.out_of_office_bot.slackCommands;

import com.slack.api.bolt.App;
import com.slack.out_of_office_bot.handlers.HelloCommandHandler;
import com.slack.out_of_office_bot.handlers.OOOCheckCommandHandler;
import com.slack.out_of_office_bot.handlers.OOOCommandHandler;
import com.slack.out_of_office_bot.handlers.OOORemoveCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Slack app where all slack commands are initialized
 */
@Configuration
public class SlackApp {

    private final HelloCommandHandler helloCommandHandler;
    private final OOOCommandHandler oooCommandHandler;
    private final OOOCheckCommandHandler oooCheckCommandHandler;
    private final OOORemoveCommandHandler oooRemoveCommandHandler;

    public SlackApp(HelloCommandHandler outOfOfficeCommandHandler,
                    OOOCommandHandler oooCommandHandler,
                    OOOCheckCommandHandler oooCheckCommandHandler,
                    OOORemoveCommandHandler oooRemoveCommandHandler) {
        this.helloCommandHandler = outOfOfficeCommandHandler;
        this.oooCommandHandler = oooCommandHandler;
        this.oooCheckCommandHandler = oooCheckCommandHandler;
        this.oooRemoveCommandHandler = oooRemoveCommandHandler;
    }

    /**
     * Initialization of all slack commands
     * @return slack bot app
     */
    @Bean
    public App initSlackApp() {
        App app = new App();
        app.command("/hello", helloCommandHandler);
        app.command("/ooo", oooCommandHandler);
        app.command("/ooo-remove", oooRemoveCommandHandler);
        app.command("/ooo-check", oooCheckCommandHandler);
        return app;
    }
}
