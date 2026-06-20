package com.example.nepalhandsbackend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonorListResponse {
    private Long Id;
    private Long campaignId;
    private String campaignTitle;
    private Integer donorId;
    private String donorName;
    private Double amount;
    private boolean anonymous;
    private LocalDateTime donatedAt;
}
