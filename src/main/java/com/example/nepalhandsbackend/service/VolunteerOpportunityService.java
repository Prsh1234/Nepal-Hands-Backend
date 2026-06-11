package com.example.nepalhandsbackend.service;


import com.example.nepalhandsbackend.dto.request.VolunteerOpportunityRequest;
import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.model.VolunteerOpportunityVerification;
import com.example.nepalhandsbackend.model.VolunteerVerificationDocument;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import com.example.nepalhandsbackend.utils.FileTextUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteerOpportunityService {
    @Autowired
    private FileTextUtils fileTextUtils;
    private final VolunteerOpportunityRepository repository;
    @Autowired
    private UserRepository userRepository;
    public Long create(
            VolunteerOpportunityRequest req,
            Integer userId
    ) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow();

        VolunteerOpportunity entity = toEntity(req);

        entity.setUser(user);
        VolunteerOpportunity created = repository.save(entity);
        return (created.getId());
    }

    @Transactional(readOnly = true)
    public VolunteerOpportunityResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<VolunteerOpportunityResponse> search(
            String category, String location, Pageable pageable) {

        Page<VolunteerOpportunityResponse> page =
                repository.search(OpportunityStatus.ACTIVE, category, location, pageable)
                        .map(this::toResponse);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<VolunteerOpportunityResponse> opportunityRequests(
            Pageable pageable) {

        Page<VolunteerOpportunityResponse> page =
                repository.opportunityRequest(OpportunityStatus.PENDING_REVIEW, pageable)
                        .map(this::toResponse);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }



    public VolunteerOpportunityResponse update(Long id, VolunteerOpportunityRequest req) throws IOException {
        VolunteerOpportunity existing = findOrThrow(id);
        applyRequest(req, existing);
        return toResponse(repository.save(existing));
    }


    public VolunteerOpportunityResponse updateStatus(Long id, OpportunityStatus status) {
        VolunteerOpportunity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        entity.setStatus(status);
        repository.save(entity);

        return toResponse(entity);
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

    private VolunteerOpportunity toEntity(VolunteerOpportunityRequest req) throws IOException {
        VolunteerOpportunity entity = new VolunteerOpportunity();
        applyRequest(req, entity);
        return entity;
    }

    private void applyRequest(VolunteerOpportunityRequest req, VolunteerOpportunity entity) throws IOException {

        entity.setTitle(req.getTitle());
        entity.setCategory(req.getCategory());
        entity.setLocation(req.getLocation());
        entity.setDescription(req.getDescription());
        entity.setLongDescription(req.getLongDescription());
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
        entity.setCoverImage(fileTextUtils.toBytes(req.getCoverImage()));
        entity.setImages(fileTextUtils.toBytesList(req.getImages()));
        entity.setContactName(req.getContactName());
        entity.setContactEmail(req.getContactEmail());
        entity.setContactPhone(req.getContactPhone());
        VolunteerOpportunityVerification verification =
                entity.getVerification();

        if (verification == null) {
            verification = new VolunteerOpportunityVerification();
            verification.setOpportunity(entity);

            // IMPORTANT
            verification.setDocuments(new ArrayList<>());
        }
        if (verification.getDocuments() == null) {
            verification.setDocuments(new ArrayList<>());
        }
// verification details

        verification.setOrgLegalName(req.getOrgLegalName());
        verification.setOrgType(req.getOrgType());
        verification.setOrgAddress(req.getOrgAddress());
        verification.setRegNumber(req.getRegNumber());
        verification.setRegAuthority(req.getRegAuthority());
        verification.setRegDate(req.getRegDate());
        verification.setPanNumber(req.getPanNumber());
        verification.setWebsite(req.getWebsite());
        verification.setOfficialEmail(req.getOfficialEmail());
        verification.setOfficialPhone(req.getOfficialPhone());
        verification.setAuthorizedSignatory(req.getAuthorizedSignatory());
        verification.setSignatoryRole(req.getSignatoryRole());


// documents

        if (req.getDocuments() != null
                && req.getDocumentTypes() != null) {

            // prevent index mismatch
            int size = Math.min(
                    req.getDocuments().size(),
                    req.getDocumentTypes().size()
            );

            // clear old docs only when new docs are uploaded
            verification.getDocuments().clear();

            for (int i = 0; i < size; i++) {

                if (req.getDocuments().get(i) == null
                        || req.getDocuments().get(i).isEmpty()) {
                    continue;
                }

                VolunteerVerificationDocument doc =
                        new VolunteerVerificationDocument();
                doc.setFileName(req.getDocuments().get(i).getOriginalFilename());
                doc.setDocumentType(
                        req.getDocumentTypes().get(i)
                );

                doc.setFile(
                        fileTextUtils.toBytes(
                                req.getDocuments().get(i)
                        )
                );
                doc.setContentType(req.getDocuments().get(i).getContentType());

                doc.setVerification(verification);

                verification.getDocuments().add(doc);
            }
        }

        entity.setVerification(verification);
    }
    private VolunteerOpportunityVerificationResponse mapVerification(
            VolunteerOpportunityVerification v
    ) {
        if (v == null) return null;

        return VolunteerOpportunityVerificationResponse.builder()
                .orgLegalName(v.getOrgLegalName())
                .orgType(v.getOrgType())
                .orgAddress(v.getOrgAddress())
                .regNumber(v.getRegNumber())
                .regAuthority(v.getRegAuthority())
                .regDate(v.getRegDate())
                .panNumber(v.getPanNumber())
                .website(v.getWebsite())
                .officialEmail(v.getOfficialEmail())
                .officialPhone(v.getOfficialPhone())
                .authorizedSignatory(v.getAuthorizedSignatory())
                .signatoryRole(v.getSignatoryRole())

                // ✅ FIX: map documents
                .documents(
                        v.getDocuments() == null ? List.of() :
                                v.getDocuments().stream().map(d ->
                                        VolunteerVerificationDocumentResponse.builder()
                                                .id(d.getId())
                                                .documentType(d.getDocumentType())
                                                .fileName(d.getFileName())
                                                .build()
                                ).toList()
                )
                .build();
    }

    private VolunteerOpportunityResponse toResponse(VolunteerOpportunity e) {
        return VolunteerOpportunityResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .location(e.getLocation())
                .description(e.getDescription())
                .longDescription(e.getLongDescription())
                .requiredSkills(e.getRequiredSkills())
                .volunteerSpots(e.getVolunteerSpots())
                .minimumAge(e.getMinimumAge())
                .commitmentType(e.getCommitmentType())
                .requirements(fileTextUtils.splitLines(e.getRequirements()))
                .activities(fileTextUtils.splitLines(e.getActivities()))
                .whyItMatters(e.getWhyItMatters())
                .benefits(fileTextUtils.splitLines(e.getBenefits()))
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .dailyHours(e.getDailyHours())
                .coverImage(e.getCoverImage())
                .images(e.getImages())
                .contactName(e.getContactName())
                .contactEmail(e.getContactEmail())
                .contactPhone(e.getContactPhone())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .verification(mapVerification(e.getVerification()))
                .postedById(
                        e.getUser() != null ? e.getUser().getId() : null
                )
                .postedByName(
                        e.getUser() != null
                                ? e.getUser().getFirstName() + " " + e.getUser().getLastName()
                                : null
                )
                .updates(
                        e.getUpdates() == null ? List.of() :
                                e.getUpdates().stream().map(u ->
                                        VolunteerOpportunityUpdateResponse.builder()
                                                .id(u.getId())
                                                .title(u.getTitle())
                                                .body(u.getBody())
                                                .date(u.getCreatedAt())
                                                .build()
                                ).toList()
                )
                .build();
    }




}