package com.example.nepalhandsbackend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonationDashboardResponse {
    private Double totalRaised;
    private Long totalDonors;
    private Double averageDonation;
}
