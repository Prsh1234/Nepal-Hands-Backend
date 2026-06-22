package com.example.nepalhandsbackend.dto.response;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VolunteerChatResponse {
    private Long id;
    private Long opportunityId;
    private Integer senderId;
    private String senderName;
    private String content;
    private Instant sentAt;
}
