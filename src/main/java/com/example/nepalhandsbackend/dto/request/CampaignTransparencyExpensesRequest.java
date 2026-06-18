package com.example.nepalhandsbackend.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class CampaignTransparencyExpensesRequest {
    private Long campaignId;
    private String category;
    private String vendor;
    private Double amount;
    private String fileName;
    private MultipartFile file;
    private LocalDate date;
}
