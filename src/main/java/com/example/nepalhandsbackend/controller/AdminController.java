package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.KycResponse;
import com.example.nepalhandsbackend.dto.response.PageResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.CampaignVerificationDocument;
import com.example.nepalhandsbackend.model.VolunteerVerificationDocument;
import com.example.nepalhandsbackend.repository.CampaignVerificationDocumentRepository;
import com.example.nepalhandsbackend.repository.VolunteerVerificationDocumentRepository;
import com.example.nepalhandsbackend.service.CampaignService;
import com.example.nepalhandsbackend.service.KycService;
import com.example.nepalhandsbackend.service.VolunteerOpportunityService;
import com.example.nepalhandsbackend.states.CampaignStatus;
import com.example.nepalhandsbackend.states.KycStatus;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import jakarta.transaction.Transactional;
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
@Transactional
public class AdminController {
    private final VolunteerOpportunityService volunteerService;
    private final CampaignService campaignService;
    private final KycService kycService;
    @Autowired
    private VolunteerVerificationDocumentRepository volunteerVerificationDocumentRepository;
    @Autowired
    private CampaignVerificationDocumentRepository campaignVerificationDocumentRepository;

    @GetMapping("/volunteer-opportunities")
    public ResponseEntity<PageResponse<VolunteerOpportunityResponse>> searchVolunteerOpportunity(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(volunteerService.opportunityRequests(pageable));
    }
    @GetMapping("/volunteer-opportunities/documents/{docId}")
    public ResponseEntity<byte[]> downloadVolunteerDocument(@PathVariable Long docId) {

        VolunteerVerificationDocument doc =
                volunteerVerificationDocumentRepository.findById(docId)
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
    public ResponseEntity<VolunteerOpportunityResponse> updateVolunteerStatus(
            @PathVariable Long id,
            @RequestParam OpportunityStatus status) {
        return ResponseEntity.ok(volunteerService.updateStatus(id, status));
    }
    @Transactional
    @DeleteMapping("/volunteer-opportunities/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        volunteerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/campaign")
    public ResponseEntity<PageResponse<CampaignResponse>> searchCampaigns(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(campaignService.campaignRequests(pageable));
    }
    @GetMapping("/campaign/documents/{docId}")
    public ResponseEntity<byte[]> downloadCampaignDocument(@PathVariable Long docId) {

        CampaignVerificationDocument doc =
                campaignVerificationDocumentRepository.findById(docId)
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
    @PatchMapping("/campaign/{id}/status")
    public ResponseEntity<CampaignResponse> updateCampaignStatus(
            @PathVariable Long id,
            @RequestParam CampaignStatus status) {
        return ResponseEntity.ok(campaignService.updateStatus(id, status));
    }
    @GetMapping("/kyc")
    public ResponseEntity<Page<KycResponse>> getByStatus(
            @RequestParam KycStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(kycService.findByStatus(status, pageable));
    }

    @PatchMapping("/kyc/{id}/status")
    public ResponseEntity<KycResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam KycStatus status,
            @RequestParam(required = false) String reason
    ) {
        return ResponseEntity.ok(kycService.updateStatus(id, status, reason));
    }
}
