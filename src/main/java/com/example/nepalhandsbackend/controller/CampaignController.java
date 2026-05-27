package com.example.nepalhandsbackend.controller;


import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.CampaignService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/organizer/campaign")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Campaign> createCampaign(
            @ModelAttribute CampaignRequest request,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        String token = authHeader.substring(7); // remove "Bearer "

        String email = jwtUtil.extractEmail(token);

        Integer userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
        return ResponseEntity.ok(campaignService.createCampaign(request,userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }
}
