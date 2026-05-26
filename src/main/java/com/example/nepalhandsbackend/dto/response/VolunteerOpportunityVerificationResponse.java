package com.example.nepalhandsbackend.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerOpportunityVerificationResponse {

    private String orgLegalName;
    private String orgType;
    private String orgAddress;

    private String regNumber;
    private String regAuthority;
    private LocalDate regDate;

    private String panNumber;
    private String website;

    private String officialEmail;
    private String officialPhone;

    private String authorizedSignatory;
    private String signatoryRole;
    private List<VolunteerVerificationDocumentResponse> documents;
}