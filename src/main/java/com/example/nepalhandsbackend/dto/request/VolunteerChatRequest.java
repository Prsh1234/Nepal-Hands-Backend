package com.example.nepalhandsbackend.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VolunteerChatRequest {

    private Long opportunityId;
    private Integer senderId;
    private String senderName;
    private String content;
    private Instant sentAt;
}