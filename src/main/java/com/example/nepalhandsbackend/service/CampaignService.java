package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.utils.FileTextUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CampaignService {
    @Autowired
    private FileTextUtils fileTextUtils;
    private final CampaignRepository campaignRepository;

    public Campaign createCampaign(CampaignRequest request) throws IOException {

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
                .coverImage(fileTextUtils.toBytes(request.getCoverImage()))
                .images(fileTextUtils.toBytesList(request.getImages()))
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
                .projectScope(fileTextUtils.splitLines(e.getProjectScope()))
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

}
