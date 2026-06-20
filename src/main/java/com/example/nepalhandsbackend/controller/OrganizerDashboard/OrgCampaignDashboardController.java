package com.example.nepalhandsbackend.controller.OrganizerDashboard;


import com.example.nepalhandsbackend.dto.request.CampaignTransparencyExpensesRequest;
import com.example.nepalhandsbackend.dto.request.CampaignTransparencyImpactRequest;
import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.model.CampaignTransparencyImpact;
import com.example.nepalhandsbackend.model.CampaignVerificationDocument;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import com.example.nepalhandsbackend.repository.CampaignTransparencyImpactRepository;
import com.example.nepalhandsbackend.service.*;
import com.example.nepalhandsbackend.states.PaymentStatus;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
    @Autowired
    private CampaignImpactService campaignImpactService;
    @Autowired
    private CampaignTransparencyExpensesRepository campaignTransparencyExpensesRepository;
    @Autowired
    private CampaignTransparencyImpactRepository campaignTransparencyImpactRepository;
    @Autowired
    private DonationService donationService;

    private final JwtUtil jwtUtil;
    @GetMapping("/campaigns")
    public ResponseEntity<Page<OrganizerCampaignResponse>> getMyCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                organizerDashboardService.getMyCampaigns(
                        userId,
                        page,
                        size,
                        direction)
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















    @PostMapping(value = "/campaign/transparency/impact", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createImpactUpdate(
            @ModelAttribute CampaignTransparencyImpactRequest req,
            @RequestHeader("Authorization") String authHeader) throws IOException {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        campaignImpactService.createImpactUpdate(userId, req);
        campaignImpactService.createImpactUpdate(userId, req);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/campaign/transparency/impact")
    public ResponseEntity<Page<CampaignTransparencyImpactResponse>> getCampaignImpacts(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                campaignImpactService.getImpactDashboard(
                        userId,
                        campaignId,
                        page,
                        size,
                        direction
                )
        );
    }

    @GetMapping("/campaign/transparency/impact/{impactId}")
    public ResponseEntity<byte[]> downloadImpactDocument(@PathVariable Long impactId) {

        CampaignTransparencyImpact doc =
                campaignTransparencyImpactRepository.findById(impactId)
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


    @GetMapping("/campaign/donationList")
    public ResponseEntity<Page<DonorListResponse>> getCampaignDonationList(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                donationService.getDonations(
                        campaignId,
                        userId,
                        page,
                        size,
                        direction
                )
        );
    }

    @GetMapping("/campaign/donationDashboard")
    public ResponseEntity<DonationDashboardResponse> getCampaignDonationDashboard(
            @RequestParam(required = false) Long campaignId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                donationService.getDonationDashboard(
                        campaignId,
                        userId
                )
        );

    }
    @GetMapping("/campaign/recentDonations")
    public ResponseEntity<List<DonorResponse>> getRecentDonations(
            @RequestParam(required = false) Long campaignId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);
        Long organizerId = userId.longValue();

        return ResponseEntity.ok(
                donationService.getRecentDonationsForOrganizer(
                        organizerId
                )
        );

    }
    @GetMapping(value = "/campaign/donations/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportDonations(
            @RequestParam(required = false) Long campaignId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        StringBuilder csv = new StringBuilder();

        csv.append("Donor Name,Campaign,Amount,Date\n");

        List<CampaignPayment> donations =
                donationService.getAllDonationsForExport(
                        campaignId,
                        userId
                );

        donations.forEach(d -> {
            csv.append("\"")
                    .append(d.isAnonymous()
                            ? "Anonymous Donor"
                            : d.getUser().getFirstName() + " " + d.getUser().getLastName())
                    .append("\",")
                    .append("\"")
                    .append(d.getCampaign().getTitle())
                    .append("\",")
                    .append(d.getAmount())
                    .append(",")
                    .append(d.getCreatedAt())
                    .append("\n");
        });

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=donations.csv"
                )
                .body(csv.toString().getBytes());
    }
}
