package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.request.KycRequest;
import com.example.nepalhandsbackend.dto.response.KycResponse;
import com.example.nepalhandsbackend.dto.response.PageResponse;
import com.example.nepalhandsbackend.model.Kyc;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.KycService;
import com.example.nepalhandsbackend.states.KycStatus;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycContoller {

    private final KycService kycService;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kyc> createCampaign(
            @ModelAttribute KycRequest request,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        String token = authHeader.substring(7); // remove "Bearer "

        String email = jwtUtil.extractEmail(token);

        Integer userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
        return ResponseEntity.ok(kycService.postKyc(request,userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KycResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(kycService.getById(id));
    }
}
