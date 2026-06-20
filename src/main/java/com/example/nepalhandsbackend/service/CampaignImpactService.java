package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CampaignTransparencyExpensesRequest;
import com.example.nepalhandsbackend.dto.request.CampaignTransparencyImpactRequest;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyExpensesResponse;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyImpactResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.model.CampaignTransparencyImpact;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import com.example.nepalhandsbackend.repository.CampaignTransparencyImpactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CampaignImpactService {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignTransparencyImpactRepository campaignTransparencyImpactRepository;

    public void createImpactUpdate(Integer userId, CampaignTransparencyImpactRequest req) throws IOException {

        Campaign campaign = campaignRepository.findById(req.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        // 🔒 Security check: only owner can post updates
        if (campaign.getUser().getId() != userId) {
            throw new RuntimeException("You are not allowed to post updates for this campaign");
        }

        CampaignTransparencyImpact update = CampaignTransparencyImpact.builder()
                .campaign(campaign)
                .type(req.getType())
                .fileName(req.getFileName())
                .contentType(req.getFile().getContentType())
                .file(req.getFile().getBytes())
                .build();


        campaignTransparencyImpactRepository.save(update);
    }
    public Page<CampaignTransparencyImpactResponse> getImpactDashboard(
            Integer userId,
            Long campaignId,
            int page,
            int size,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("uploadedAt").ascending()
                : Sort.by("uploadedAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CampaignTransparencyImpact> impacts;

        if (campaignId != null) {

            impacts = campaignTransparencyImpactRepository
                    .findByCampaign_IdAndCampaign_User_Id(
                            campaignId,
                            userId,
                            pageable
                    );

        } else {

            impacts = campaignTransparencyImpactRepository
                    .findByCampaign_User_Id(
                            userId,
                            pageable
                    );
        }

        return impacts.map(u ->
                CampaignTransparencyImpactResponse.builder()
                        .id(u.getId())
                        .campaignId(u.getCampaign().getId())
                        .campaignTitle(u.getCampaign().getTitle())
                        .fileName(u.getFileName())
                        .contentType(u.getContentType())
                        .uploadedAt(u.getUploadedAt())
                        .type(u.getType())
                        .build()
        );
    }

    public List<CampaignTransparencyImpactResponse> getImpacts(

            Long campaignId) {



        List<CampaignTransparencyImpact> impacts = campaignTransparencyImpactRepository
                .findByCampaign_Id(
                        campaignId
                );;


        return impacts.stream()
                .map(expense -> CampaignTransparencyImpactResponse.builder()
                        .id(expense.getId())
                        .campaignId(expense.getCampaign().getId())
                        .campaignTitle(expense.getCampaign().getTitle())
                        .type(expense.getType())
                        .fileName(expense.getFileName())
                        .uploadedAt(expense.getUploadedAt())
                        .contentType(expense.getContentType())
                        .type(expense.getType())
                        .build())
                .toList();
    }
}
