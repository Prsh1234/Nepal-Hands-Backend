package com.example.nepalhandsbackend.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
public class CampaignTransparencyImpactRequest {
    private Long campaignId;
    private String type;
    private String fileName;
    private MultipartFile file;
}
