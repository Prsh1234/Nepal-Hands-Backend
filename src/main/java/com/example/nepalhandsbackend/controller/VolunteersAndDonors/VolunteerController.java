package com.example.nepalhandsbackend.controller.VolunteersAndDonors;

import com.example.nepalhandsbackend.dto.request.VolunteerApplyRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.VolunteerOpportunityService;
import com.example.nepalhandsbackend.service.VolunteersAndDonors.VolunteersService;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteer")
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerOpportunityService volunteerOpportunityService;

    private final VolunteersService service;
    private final JwtUtil jwtUtil;

    /** GET /api/organizer/volunteer-opportunities/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerOpportunityService.getById(id));
    }

    @GetMapping("/applicationStatus/{id}")
    public ResponseEntity<ApplicationStatus> getVolunteerApplicationStatus(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(service.getStatus(id, userId));
    }
    @PostMapping("/apply/{id}")
    public ResponseEntity<Map<String, Object>> apply(
            @PathVariable Long id,
            @RequestBody VolunteerApplyRequest request,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);

            service.apply(userId, request, id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Application submitted successfully"
            ));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
