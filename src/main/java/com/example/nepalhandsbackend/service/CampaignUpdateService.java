package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.CampaignUpdateResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignUpdate;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.CampaignUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignUpdateService {

    private final CampaignUpdateRepository campaignUpdateRepository;
    private final CampaignRepository campaignRepository;

    public void createUpdate(Integer userId, CreateUpdateRequest req) {

        Campaign campaign = campaignRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        // 🔒 Security check: only owner can post updates
        if (campaign.getUser().getId() != userId) {
            throw new RuntimeException("You are not allowed to post updates for this campaign");
        }

        CampaignUpdate update = CampaignUpdate.builder()
                .campaign(campaign)
                .title(req.getTitle())
                .body(req.getBody())
                .build();

        campaignUpdateRepository.save(update);
    }
    public Page<CampaignUpdateResponse> getUpdates(
            Integer userId,
            Long campaignId,
            int page,
            int size,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CampaignUpdate> updates;

        if (campaignId != null) {

            updates = campaignUpdateRepository
                    .findByCampaign_IdAndCampaign_User_Id(
                            campaignId,
                            userId,
                            pageable
                    );

        } else {

            updates = campaignUpdateRepository
                    .findByCampaign_User_Id(
                            userId,
                            pageable
                    );
        }

        return updates.map(u ->
                CampaignUpdateResponse.builder()
                        .id(u.getId())
                        .campaignId(u.getCampaign().getId())
                        .campaignTitle(u.getCampaign().getTitle())
                        .title(u.getTitle())
                        .body(u.getBody())
                        .date(u.getCreatedAt())
                        .build()
        );
    }
}
