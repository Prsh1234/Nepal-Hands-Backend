package com.example.nepalhandsbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignVerificationDocumentResponse {
    private Long id;
    private String documentType;
    private String fileName;
    private byte[] file;
}
