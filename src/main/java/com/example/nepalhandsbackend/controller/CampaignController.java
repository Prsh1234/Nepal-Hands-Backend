package com.example.nepalhandsbackend.controller;


import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.service.CampaignService;
import com.example.nepalhandsbackend.states.CampaignStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/organizer/campaign")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Campaign> createCampaign(
            @ModelAttribute CampaignRequest request
    ) throws IOException {

        return ResponseEntity.ok(campaignService.createCampaign(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }
}
