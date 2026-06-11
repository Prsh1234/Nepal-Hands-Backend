package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CampaignUpdateResponse {

    private Long id;
    private Long campaignId;
    private String campaignTitle;

    private String title;
    private String body;
    private LocalDateTime date;
}