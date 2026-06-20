package com.example.nepalhandsbackend.controller.VolunteersAndDonors;


import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyExpensesResponse;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyImpactResponse;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.model.CampaignTransparencyImpact;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import com.example.nepalhandsbackend.repository.CampaignTransparencyImpactRepository;
import com.example.nepalhandsbackend.service.CampaignExpensesService;
import com.example.nepalhandsbackend.service.CampaignImpactService;
import com.example.nepalhandsbackend.service.CampaignService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteer/campaign")
@RequiredArgsConstructor
public class DonorController {

    @Autowired
    private CampaignService campaignService;
    private final JwtUtil jwtUtil;
    @Autowired
    private CampaignExpensesService campaignExpensesService;
    @Autowired
    private CampaignImpactService campaignImpactService;


    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }

    @GetMapping("/transparency/expenses/{campaignId}")
    public ResponseEntity<List<CampaignTransparencyExpensesResponse>> getCampaignExpenses(
            @PathVariable Long campaignId
    ) {


        return ResponseEntity.ok(
                campaignExpensesService.getExpenses(

                        campaignId
                )
        );
    }
    @Autowired
    private CampaignTransparencyExpensesRepository campaignTransparencyExpensesRepository;
    @GetMapping("/transparency/expenses/file/{expenseId}")
    public ResponseEntity<byte[]> downloadExpensesDocument(@PathVariable Long expenseId) {

        CampaignTransparencyExpenses doc =
                campaignTransparencyExpensesRepository.findById(expenseId)
                        .orElseThrow();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                doc.getContentType() != null ? doc.getContentType() : "application/pdf"
                        )
                )
                .header("Content-Disposition",
                        "inline; filename=\"" + doc.getFileName() + "\"")
                .body(doc.getFile());
    }
    @GetMapping("/transparency/impacts/{campaignId}")
    public ResponseEntity<List<CampaignTransparencyImpactResponse>> getCampaignImpacts(
            @PathVariable Long campaignId
    ) {


        return ResponseEntity.ok(
                campaignImpactService.getImpacts(

                        campaignId
                )
        );
    }
    @Autowired
    private CampaignTransparencyImpactRepository campaignTransparencyImpactRepository;
    @GetMapping("/transparency/impacts/file/{expenseId}")
    public ResponseEntity<byte[]> downloadImpactDocument(@PathVariable Long expenseId) {

        CampaignTransparencyImpact doc =
                campaignTransparencyImpactRepository.findById(expenseId)
                        .orElseThrow();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                doc.getContentType() != null ? doc.getContentType() : "application/pdf"
                        )
                )
                .header("Content-Disposition",
                        "inline; filename=\"" + doc.getFileName() + "\"")
                .body(doc.getFile());
    }
}