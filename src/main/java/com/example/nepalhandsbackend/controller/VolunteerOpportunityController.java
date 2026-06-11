package com.example.nepalhandsbackend.controller;


import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.PageResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.VolunteerOpportunityService;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import com.example.nepalhandsbackend.utils.JwtFilter;
import com.example.nepalhandsbackend.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/organizer/volunteer-opportunities")
@RequiredArgsConstructor
public class VolunteerOpportunityController {
    public record CreateVolunteerResponse(Long id) {}
    private final VolunteerOpportunityService service;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    /** POST /api/organizer/volunteer-opportunities — submit the form */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateVolunteerResponse> create(
            @ModelAttribute VolunteerOpportunityRequest request,
            @RequestHeader("Authorization") String authHeader) throws IOException {
        String token = authHeader.substring(7); // remove "Bearer "

        String email = jwtUtil.extractEmail(token);

        Integer userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateVolunteerResponse(service.create(request,userId)));
    }

    /** GET /api/organizer/volunteer-opportunities/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /** GET /api/organizer/volunteer-opportunities?category=teaching&location=Gorkha&page=0&size=10 */
    @GetMapping
    public ResponseEntity<PageResponse<VolunteerOpportunityResponse>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(service.search(category, location, pageable));
    }


    /** PUT /api/organizer/volunteer-opportunities/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VolunteerOpportunityRequest request) throws IOException {
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