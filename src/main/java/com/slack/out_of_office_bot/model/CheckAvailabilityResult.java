package com.slack.out_of_office_bot.model;

import com.slack.out_of_office_bot.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckAvailabilityResult {
    private AvailabilityStatus availabilityStatus;
    private LocalDateTime start;
    private LocalDateTime end;

    public CheckAvailabilityResult(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
