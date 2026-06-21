package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.model.VolunteerOpportunityUpdate;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import com.example.nepalhandsbackend.states.VolunteerCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VolunteerOpportunityResponse {

    private Long id;
    private Integer postedById;
    private String postedByName;
    private String title;
    private VolunteerCategory category;
    private String location;
    private String description;
    private String longDescription;
    private Integer totalSpots;
    private Long filledSpots;
    private List<String> requiredSkills;
    private Integer volunteerSpots;
    private Integer minimumAge;
    private String commitmentType;
    private List<String> requirements;

    private List<String> activities;   // parsed from newline-separated string
    private String whyItMatters;
    private List<String> benefits;     // parsed from newline-separated string

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer dailyHours;

    private byte[] coverImage;
    private List<byte[]> images;

    private String contactName;
    private String contactEmail;
    private String contactPhone;



    private OpportunityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private VolunteerOpportunityVerificationResponse verification;
    private List<VolunteerOpportunityUpdateResponse> updates;
    private List<VolunteerTeamResponse> team;
}