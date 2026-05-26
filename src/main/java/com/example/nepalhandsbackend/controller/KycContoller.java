package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.request.KycRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.KycResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.Kyc;
import com.example.nepalhandsbackend.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycContoller {

    private final KycService kycService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kyc> createCampaign(
            @ModelAttribute KycRequest request
    ) throws IOException {

        return ResponseEntity.ok(kycService.postKyc(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KycResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(kycService.getById(id));
    }
}
