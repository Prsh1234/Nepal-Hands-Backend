package com.example.nepalhandsbackend.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignVerificationResponse {

    private String orgLegalName;
    private String orgType;

    private String regNumber;
    private String regAuthority;
    private LocalDate regDate;

    private String panNumber;
    private String website;

    private String bankName;
    private String bankAccountHolderName;
    private String bankAccountNumber;

    private String authorizedSignatory;
    private String signatoryRole;
    private List<CampaignVerificationDocumentResponse> documents;
}
