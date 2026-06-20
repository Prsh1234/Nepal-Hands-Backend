package com.example.nepalhandsbackend.controller.OrganizerDashboard;

import com.example.nepalhandsbackend.dto.response.DashboardStatsResponse;
import com.example.nepalhandsbackend.dto.response.OrganizerCampaignResponse;
import com.example.nepalhandsbackend.service.OrganizerDashboardService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer/dashboard")
@RequiredArgsConstructor
public class OrganizerDashboard {
    private final JwtUtil jwtUtil;
    @Autowired
    private OrganizerDashboardService organizerDashboardService;
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getMyCampaigns(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getDashboardStats((long) userId)
        );
    }
}
