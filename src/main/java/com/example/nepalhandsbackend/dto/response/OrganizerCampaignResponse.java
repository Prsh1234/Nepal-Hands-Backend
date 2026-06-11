package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrganizerCampaignResponse {

    private Long id;
    private String title;
    private String category;
    private String status;

    private Long goal;
    private Long raised;

    private Integer donors;
    private Long daysLeft;

    private LocalDateTime createdDate;
}