package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VolunteerTeamResponse {
    private Long id;
    private int volunteerId;
    private String fullName;
    private Long opportunityId;
    private LocalDateTime joinedAt;
}
