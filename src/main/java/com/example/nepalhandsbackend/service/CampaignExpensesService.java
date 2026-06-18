package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CampaignTransparencyExpensesRequest;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyExpensesResponse;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CampaignExpensesService {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignTransparencyExpensesRepository campaignTransparencyExpensesRepository;
    public void createExpenseUpdate(Integer userId, CampaignTransparencyExpensesRequest req) throws IOException {

        Campaign campaign = campaignRepository.findById(req.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        // 🔒 Security check: only owner can post updates
        if (campaign.getUser().getId() != userId) {
            throw new RuntimeException("You are not allowed to post updates for this campaign");
        }

        CampaignTransparencyExpenses update = CampaignTransparencyExpenses.builder()
                .campaign(campaign)
                .category(req.getCategory())
                .vendor(req.getVendor())
                .date(req.getDate())
                .amount(req.getAmount())
                .fileName(req.getFileName())
                .contentType(req.getFile().getContentType())
                .file(req.getFile().getBytes())
                .build();


        campaignTransparencyExpensesRepository.save(update);
    }
    public Page<CampaignTransparencyExpensesResponse> getExpensesDashboard(
            Integer userId,
            Long campaignId,
            int page,
            int size,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("date").ascending()
                : Sort.by("date").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CampaignTransparencyExpenses> expenses;

        if (campaignId != null) {

            expenses = campaignTransparencyExpensesRepository
                    .findByCampaign_IdAndCampaign_User_Id(
                            campaignId,
                            userId,
                            pageable
                    );

        } else {

            expenses = campaignTransparencyExpensesRepository
                    .findByCampaign_User_Id(
                            userId,
                            pageable
                    );
        }

        return expenses.map(u ->
                CampaignTransparencyExpensesResponse.builder()
                        .id(u.getId())
                        .campaignId(u.getCampaign().getId())
                        .campaignTitle(u.getCampaign().getTitle())
                        .category(u.getCategory())
                        .amount(u.getAmount())
                        .vendor(u.getVendor())
                        .fileName(u.getFileName())
                        .date(u.getDate())
                        .contentType(u.getContentType())
                        .build()
        );
    }

    public List<CampaignTransparencyExpensesResponse> getExpenses(

            Long campaignId) {



            List<CampaignTransparencyExpenses> expenses = campaignTransparencyExpensesRepository
                    .findByCampaign_Id(
                            campaignId
                    );;


        return expenses.stream()
                .map(expense -> CampaignTransparencyExpensesResponse.builder()
                        .id(expense.getId())
                        .campaignId(expense.getCampaign().getId())
                        .campaignTitle(expense.getCampaign().getTitle())
                        .category(expense.getCategory())
                        .amount(expense.getAmount())
                        .vendor(expense.getVendor())
                        .fileName(expense.getFileName())
                        .date(expense.getDate())
                        .contentType(expense.getContentType())
                        .build())
                .toList();
    }
}
