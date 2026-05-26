package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.response.PageResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.VolunteerVerificationDocument;
import com.example.nepalhandsbackend.repository.VolunteerVerificationDocumentRepository;
import com.example.nepalhandsbackend.service.VolunteerOpportunityService;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final VolunteerOpportunityService service;
    @Autowired
    private VolunteerVerificationDocumentRepository documentRepository;

    @GetMapping("/volunteer-opportunities")
    public ResponseEntity<PageResponse<VolunteerOpportunityResponse>> search(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(service.opportunityRequests(pageable));
    }
    @GetMapping("/volunteer-opportunities/documents/{docId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long docId) {

        VolunteerVerificationDocument doc =
                documentRepository.findById(docId)
                        .orElseThrow();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                doc.getContentType() != null ? doc.getContentType() : "application/pdf"
                        )
                )
                .header("Content-Disposition",
                        "inline; filename=\"" + doc.getFileName() + "\"")
                .body(doc.getFile());
    }
    @PatchMapping("/volunteer-opportunities/{id}/status")
    public ResponseEntity<VolunteerOpportunityResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OpportunityStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

}
