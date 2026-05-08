package com.example.nepalhandsbackend.service;


import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerOpportunityService {

    private final VolunteerOpportunityRepository repository;

    public VolunteerOpportunityResponse create(VolunteerOpportunityRequest req) {
        VolunteerOpportunity entity = toEntity(req);
        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public VolunteerOpportunityResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<VolunteerOpportunityResponse> search(
            String category, String location, Pageable pageable) {
        return repository
                .search(OpportunityStatus.ACTIVE, category, location, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<VolunteerOpportunityResponse> getByCampaign(String campaignId, Pageable pageable) {
        return repository.findByLinkedCampaignId(campaignId, pageable).map(this::toResponse);
    }

    public VolunteerOpportunityResponse update(Long id, VolunteerOpportunityRequest req) {
        VolunteerOpportunity existing = findOrThrow(id);
        applyRequest(req, existing);
        return toResponse(repository.save(existing));
    }

    public VolunteerOpportunityResponse updateStatus(Long id, OpportunityStatus status) {
        VolunteerOpportunity entity = findOrThrow(id);
        entity.setStatus(status);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private VolunteerOpportunity findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Volunteer opportunity not found: " + id));
    }

    private VolunteerOpportunity toEntity(VolunteerOpportunityRequest req) {
        VolunteerOpportunity entity = new VolunteerOpportunity();
        applyRequest(req, entity);
        return entity;
    }

    private void applyRequest(VolunteerOpportunityRequest req, VolunteerOpportunity entity) {
        entity.setTitle(req.getTitle());
        entity.setCategory(req.getCategory());
        entity.setLocation(req.getLocation());
        entity.setDescription(req.getDescription());
        entity.setLongDescription(req.getLongDescription());
        entity.setLinkedCampaignId(req.getLinkedCampaignId());
        entity.setRequiredSkills(req.getRequiredSkills());
        entity.setVolunteerSpots(req.getVolunteerSpots());
        entity.setMinimumAge(req.getMinimumAge() != null ? req.getMinimumAge() : 18);
        entity.setCommitmentType(req.getCommitmentType());
        entity.setRequirements(req.getRequirements());
        entity.setActivities(req.getActivities());
        entity.setWhyItMatters(req.getWhyItMatters());
        entity.setBenefits(req.getBenefits());
        entity.setStartDate(req.getStartDate());
        entity.setEndDate(req.getEndDate());
        entity.setDailyHours(req.getDailyHours() != null ? req.getDailyHours() : 6);
        entity.setContactName(req.getContactName());
        entity.setContactEmail(req.getContactEmail());
        entity.setContactPhone(req.getContactPhone());
    }

    private VolunteerOpportunityResponse toResponse(VolunteerOpportunity e) {
        return VolunteerOpportunityResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .location(e.getLocation())
                .description(e.getDescription())
                .longDescription(e.getLongDescription())
                .linkedCampaignId(e.getLinkedCampaignId())
                .requiredSkills(e.getRequiredSkills())
                .volunteerSpots(e.getVolunteerSpots())
                .minimumAge(e.getMinimumAge())
                .commitmentType(e.getCommitmentType())
                .requirements(splitLines(e.getRequirements()))
                .activities(splitLines(e.getActivities()))
                .whyItMatters(e.getWhyItMatters())
                .benefits(splitLines(e.getBenefits()))
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .dailyHours(e.getDailyHours())
                .contactName(e.getContactName())
                .contactEmail(e.getContactEmail())
                .contactPhone(e.getContactPhone())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private List<String> splitLines(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}