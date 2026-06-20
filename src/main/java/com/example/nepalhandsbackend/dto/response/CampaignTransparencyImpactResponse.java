package com.example.nepalhandsbackend.dto.response;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class CampaignTransparencyImpactResponse {
    private Long id;
    private Long campaignId;
    private String campaignTitle;
    private String type;
    private String fileName;
    private byte[] file;
    private String contentType;
    private LocalDate uploadedAt;
}