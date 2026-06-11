package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class VolunteerOpportunityUpdateResponse {

    private Long id;
    private Long opportunityId;
    private String opportunityTitle;

    private String title;
    private String body;
    private LocalDateTime date;
}
