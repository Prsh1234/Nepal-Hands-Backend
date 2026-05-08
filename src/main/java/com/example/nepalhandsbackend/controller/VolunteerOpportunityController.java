package com.example.nepalhandsbackend.controller;


import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.service.VolunteerOpportunityService;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/organizer/volunteer-opportunities")
@RequiredArgsConstructor
public class VolunteerOpportunityController {

    private final VolunteerOpportunityService service;

    /** POST /api/organizer/volunteer-opportunities — submit the form */
    @PostMapping
    public ResponseEntity<VolunteerOpportunityResponse> create(
            @Valid @RequestBody VolunteerOpportunityRequest request) {
        VolunteerOpportunityResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** GET /api/organizer/volunteer-opportunities/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /** GET /api/organizer/volunteer-opportunities?category=teaching&location=Gorkha&page=0&size=10 */
    @GetMapping
    public ResponseEntity<Page<VolunteerOpportunityResponse>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(service.search(category, location, pageable));
    }

    /** GET /api/organizer/volunteer-opportunities/campaign/{campaignId} */
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<Page<VolunteerOpportunityResponse>> getByCampaign(
            @PathVariable String campaignId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByCampaign(campaignId, pageable));
    }

    /** PUT /api/organizer/volunteer-opportunities/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VolunteerOpportunityRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /** PATCH /api/organizer/volunteer-opportunities/{id}/status?status=ACTIVE — admin approval */
    @PatchMapping("/{id}/status")
    public ResponseEntity<VolunteerOpportunityResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OpportunityStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    /** DELETE /api/organizer/volunteer-opportunities/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}