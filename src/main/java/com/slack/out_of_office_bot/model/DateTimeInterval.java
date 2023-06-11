package com.slack.out_of_office_bot.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * This class represents a time interval.
 * For example '12.12.2012 09:00' to '12.12.2012 11:00', where '12.12.2012 09:00' is from value and to is '12.12.2012 11:00'.
 */
@Value
@AllArgsConstructor
public class DateTimeInterval {
    LocalDateTime from;
    LocalDateTime to;
}
