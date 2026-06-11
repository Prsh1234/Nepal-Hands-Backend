package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.CampaignStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CampaignResponse {
    private Long id;
    private Integer postedById;
    private String postedByName;
    private String title;
    private String category;
    private String location;
    private String description;
    private String longDescription;
    private List<String> projectScope;

    private Long goal;
    private String duration;
    private String organizer;

    private LocalDate startDate;
    private LocalDate endDate;

    private String contactName;
    private String contactEmail;
    private String contactPhone;

    private byte[] coverImage;
    private List<byte[]> images;


    private CampaignStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CampaignVerificationResponse verification;
    private List<CampaignUpdateResponse> updates;

}
