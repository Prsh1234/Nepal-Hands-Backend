package com.example.nepalhandsbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerVerificationDocumentResponse {
    private Long id;
    private String documentType;
    private String fileName;
    // base64 or URL (you currently store bytes)
    private byte[] file;
}