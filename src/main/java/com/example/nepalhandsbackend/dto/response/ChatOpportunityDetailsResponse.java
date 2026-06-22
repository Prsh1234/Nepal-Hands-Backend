package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ChatOpportunityDetailsResponse {
    private Long id;
    private Integer organizerId;
    private String organizer;
    private String title;
    private String location;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long filledSpots;
    private List<VolunteerTeamResponse> team;

}
