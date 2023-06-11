package com.slack.out_of_office_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class OutOfOfficeBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutOfOfficeBotApplication.class, args);
		log.debug("Enviorment variables: " +
				"\nSlack_bot_token: " + System.getenv("SLACK_BOT_TOKEN") +
				"\nSlack_signing_secret: " + System.getenv("SLACK_SIGNING_SECRET"));
	}
}
