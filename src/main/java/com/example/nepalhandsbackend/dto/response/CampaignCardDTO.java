package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.CampaignCategory;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class CampaignCardDTO {

    private Long id;
    private String title;
    private CampaignCategory category;
    private String description;
    private String organizer;

    private Long goal;
    private Double raised;

    private double progress;
    private Long donors;
    private Long daysLeft;
    private LocalDateTime postedAt;



}