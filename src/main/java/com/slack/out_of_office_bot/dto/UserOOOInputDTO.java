package com.slack.out_of_office_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOOOInputDTO {

    private Long id;

    @JsonIgnore
    private String slackUserId;

    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

}
