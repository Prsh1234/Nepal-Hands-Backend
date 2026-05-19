package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign createCampaign(CampaignRequest request) throws IOException {

        byte[] coverBytes = null;
        if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
            coverBytes = request.getCoverImage().getBytes();
        }

        List<byte[]> imageBytesList = new ArrayList<>();
        if (request.getImages() != null) {
            for (MultipartFile file : request.getImages()) {
                if (!file.isEmpty()) {
                    imageBytesList.add(file.getBytes());
                }
            }
        }

        Campaign campaign = Campaign.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .location(request.getLocation())
                .description(request.getDescription())
                .longDescription(request.getLongDescription())
                .projectScope(request.getProjectScope())
                .goal(request.getGoal())
                .duration(request.getDuration())
                .organizer(request.getOrganizer())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .contactName(request.getContactName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .coverImage(coverBytes)
                .images(imageBytesList)
                .build();

        return campaignRepository.save(campaign);
    }
    @Transactional(readOnly = true)
    public CampaignResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }


    private Campaign findOrThrow(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Volunteer opportunity not found: " + id));
    }

    private CampaignResponse toResponse(Campaign e) {
        return CampaignResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .location(e.getLocation())
                .description(e.getDescription())
                .longDescription(e.getLongDescription())
                .projectScope(splitLines(e.getProjectScope()))
                .goal(e.getGoal())
                .duration(e.getDuration())
                .organizer(e.getOrganizer())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .contactName(e.getContactName())
                .contactEmail(e.getContactEmail())
                .contactPhone(e.getContactPhone())
                .coverImage(e.getCoverImage())
                .images(e.getImages())
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
