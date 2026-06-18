package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class CampaignTransparencyExpensesResponse {
    private Long id;
    private Long campaignId;
    private String campaignTitle;
    private String category;
    private String vendor;
    private Double amount;
    private String fileName;
    private byte[] file;
    private String contentType;
    private LocalDate date;
}
