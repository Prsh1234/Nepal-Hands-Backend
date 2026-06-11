package com.example.nepalhandsbackend.controller.OrganizerDashboard;


import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.service.CampaignUpdateService;
import com.example.nepalhandsbackend.service.OrganizerDashboardService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer/dashboard")
@RequiredArgsConstructor
public class OrgCampaignDashboardController {

    private final OrganizerDashboardService organizerDashboardService;
    private final CampaignUpdateService campaignUpdateService;

    private final JwtUtil jwtUtil;
    @GetMapping("/campaigns")
    public ResponseEntity<List<OrganizerCampaignResponse>> getMyCampaigns(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyCampaigns(userId)
        );
    }



    //get updates posted for volunteer Campaigns
    @GetMapping("/campaign/updates")
    public ResponseEntity<Page<CampaignUpdateResponse>> getCampaignUpdates(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                campaignUpdateService.getUpdates(
                        userId,
                        campaignId,
                        page,
                        size,
                        direction
                )
        );
    }

    //get title and id for volunteer campaigns created by current user
    @GetMapping("/campaigns/select")
    public ResponseEntity<List<CampaignSelectResponse>> getCampaignDropdown(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyCampaignDropdown(userId)
        );
    }

    //post updates for ongoing campaign
    @PostMapping("/campaign/updates")
    public ResponseEntity<Void> createUpdate(
            @RequestBody CreateUpdateRequest req,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        campaignUpdateService.createUpdate(userId, req);

        return ResponseEntity.ok().build();
    }



}
