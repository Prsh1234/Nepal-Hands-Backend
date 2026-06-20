package com.example.nepalhandsbackend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private Double totalRaised;
    private Long totalDonors;
    private Long totalApplicants;
}