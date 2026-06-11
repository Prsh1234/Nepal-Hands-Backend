package com.example.nepalhandsbackend.controller.OrganizerDashboard;

import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.OrganizerVolunteerResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerApplyResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunitySelectResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityUpdateResponse;
import com.example.nepalhandsbackend.service.OrganizerDashboardService;
import com.example.nepalhandsbackend.service.VolunteerOpportunityUpdateService;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizer/dashboard")
@RequiredArgsConstructor
public class OrgVolunteerDashboardController {
    private final OrganizerDashboardService organizerDashboardService;

    private final VolunteerOpportunityUpdateService volunteerOpportunityUpdateService;
    private final JwtUtil jwtUtil;

    @GetMapping("/volunteers")
    public ResponseEntity<List<OrganizerVolunteerResponse>> getMyVolunteers(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyOpportunities(userId)
        );
    }

    //get updates posted for volunteer Campaigns
    @GetMapping("/volunteer/updates")
    public ResponseEntity<Page<VolunteerOpportunityUpdateResponse>> getVolunteerUpdates(
            @RequestParam(required = false) Long opportunityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                volunteerOpportunityUpdateService.getUpdates(
                        userId,
                        opportunityId,
                        page,
                        size,
                        direction
                )
        );
    }

    //get title and id for volunteer campaigns created by current user
    @GetMapping("/volunteers/select")
    public ResponseEntity<List<VolunteerOpportunitySelectResponse>> getVolunteerDropdown(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyVolunteerOpportunityDropdown(userId)
        );
    }

    //post updates for ongoing volunteer campaigns
    @PostMapping("/volunteer/updates")
    public ResponseEntity<Void> createVolunteerUpdate(
            @RequestBody CreateUpdateRequest req,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        volunteerOpportunityUpdateService.createVolunteerUpdate(userId, req);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/volunteer/application-requests")
    public ResponseEntity<Page<VolunteerApplyResponse>> getVolunteerApplicationRequests(
            @RequestParam(required = false) Long opportunityId,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getApplicationResponses(
                        userId,
                        opportunityId,
                        status,
                        page,
                        size
                )
        );
    }
    @PatchMapping("/application/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveApplication(
            @PathVariable Long id) {

        organizerDashboardService.updateApplicationStatus(id, ApplicationStatus.APPROVED);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Application approved successfully"
        ));
    }

    @PatchMapping("/application/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectApplication(
            @PathVariable Long id) {

        organizerDashboardService.updateApplicationStatus(id, ApplicationStatus.REJECTED);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Application rejected successfully"
        ));
    }
}
