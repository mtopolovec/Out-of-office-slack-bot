package com.slack.out_of_office_bot.model;

import com.slack.out_of_office_bot.utility.DateTimeUtils;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Entity used to store the user out of office input values
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserOOOInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slackUserId;
    private String slackUsername;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Check if current out of office input has passed.
     * @return true if input has passed
     */
    public boolean hasPassed() {
        return this.getEndTime().isBefore(DateTimeUtils.currentTime());
    }

    /**
     * Check if out of office input is currently active
     * @return true if currently active
     */
    public boolean current() {
        return this.getStartTime().isBefore(DateTimeUtils.currentTime()) && this.getEndTime().isAfter(DateTimeUtils.currentTime());
    }

    /**
     * Check if out of office input is just for one day.
     * If input is for whole day with end time set to end of day then it is not marked as one day.
     * @return true if input is for one day
     */
    public boolean isOneDay() {
        return this.getStartTime().getDayOfMonth() == this.getEndTime().getDayOfMonth() && this.getEndTime() != DateTimeUtils.endOfDay();
    }
}
