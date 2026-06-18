package com.example.nepalhandsbackend.controller.OrganizerDashboard;


import com.example.nepalhandsbackend.dto.request.CampaignTransparencyExpensesRequest;
import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.model.CampaignVerificationDocument;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import com.example.nepalhandsbackend.service.CampaignExpensesService;
import com.example.nepalhandsbackend.service.CampaignUpdateService;
import com.example.nepalhandsbackend.service.OrganizerDashboardService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/organizer/dashboard")
@RequiredArgsConstructor
public class OrgCampaignDashboardController {

    private final OrganizerDashboardService organizerDashboardService;
    private final CampaignUpdateService campaignUpdateService;
    @Autowired
    private CampaignExpensesService campaignExpensesService;

    private final JwtUtil jwtUtil;
    @GetMapping("/campaigns")
    public ResponseEntity<List<OrganizerCampaignResponse>> getMyCampaigns(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyCampaigns(userId)
        );
    }



    //get updates posted for volunteer Campaigns
    @GetMapping("/campaign/updates")
    public ResponseEntity<Page<CampaignUpdateResponse>> getCampaignUpdates(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                campaignUpdateService.getUpdates(
                        userId,
                        campaignId,
                        page,
                        size,
                        direction
                )
        );
    }

    //get title and id for volunteer campaigns created by current user
    @GetMapping("/campaigns/select")
    public ResponseEntity<List<CampaignSelectResponse>> getCampaignDropdown(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyCampaignDropdown(userId)
        );
    }

    //post updates for ongoing campaign
    @PostMapping("/campaign/updates")
    public ResponseEntity<Void> createUpdate(
            @RequestBody CreateUpdateRequest req,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        campaignUpdateService.createUpdate(userId, req);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/campaign/transparency/expenses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createExpenseUpdate(
            @ModelAttribute CampaignTransparencyExpensesRequest req,
            @RequestHeader("Authorization") String authHeader) throws IOException {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        campaignExpensesService.createExpenseUpdate(userId, req);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/campaign/transparency/expenses")
    public ResponseEntity<Page<CampaignTransparencyExpensesResponse>> getCampaignExpenses(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                campaignExpensesService.getExpensesDashboard(
                        userId,
                        campaignId,
                        page,
                        size,
                        direction
                )
        );
    }
    @Autowired
    private CampaignTransparencyExpensesRepository campaignTransparencyExpensesRepository;
    @GetMapping("/campaign/transparency/expenses/{expenseId}")
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
}
