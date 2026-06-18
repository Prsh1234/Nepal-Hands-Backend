package com.example.nepalhandsbackend.controller.Authenticated;


import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.CampaignTransparencyExpensesResponse;
import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.repository.CampaignTransparencyExpensesRepository;
import com.example.nepalhandsbackend.service.CampaignExpensesService;
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
public class AuthenticatedCampaignController {

    @Autowired
    private CampaignService campaignService;
    private final JwtUtil jwtUtil;
    @Autowired
    private CampaignExpensesService campaignExpensesService;


    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }

    @GetMapping("/transparency/expenses/{campaignId}")
    public ResponseEntity<List<CampaignTransparencyExpensesResponse>> getCampaignExpenses(
            @PathVariable Long campaignId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

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
}