package com.example.nepalhandsbackend.controller.campaignPayment;

import com.example.nepalhandsbackend.dto.request.EsewaPaymentRequest;
import com.example.nepalhandsbackend.dto.response.EsewaSignatureResponse;
import com.example.nepalhandsbackend.dto.response.EsewaStatusResponse;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.EsewaVerificationService;
import com.example.nepalhandsbackend.service.PaymentService;
import com.example.nepalhandsbackend.states.PaymentStatus;
import com.example.nepalhandsbackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/campaign")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate-signature")
    public EsewaSignatureResponse generateSignature(@RequestBody EsewaPaymentRequest paymentRequest) {

        return paymentService.generateSignature(paymentRequest);
    }





}
