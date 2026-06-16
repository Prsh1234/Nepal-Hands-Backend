package com.example.nepalhandsbackend.controller;

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
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private CampaignPaymentRepository campaignPaymentRepository;

    @Autowired
    private EsewaVerificationService verificationService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate-signature")
    public EsewaSignatureResponse generateSignature(@RequestBody EsewaPaymentRequest paymentRequest) {

        return paymentService.generateSignature(paymentRequest);
    }


    @GetMapping("/esewa/success")
    public ResponseEntity<?> esewaSuccess(@RequestParam String data) {

        try {
            String cleanData = URLDecoder.decode(data, StandardCharsets.UTF_8);

            String json = new String(
                    Base64.getDecoder().decode(cleanData),
                    StandardCharsets.UTF_8
            );

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(json, Map.class);

            String uuid = (String) payload.get("transaction_uuid");
            double total = Double.parseDouble(payload.get("total_amount").toString());
            String productCode = (String) payload.get("product_code");

            // 🔥 CALL eSewa for real verification
            EsewaStatusResponse statusResponse =
                    verificationService.checkStatus(productCode, uuid, total);

            System.out.println("STATUS: " + statusResponse.getStatus());

            // 🔥 Find payment
            CampaignPayment payment = campaignPaymentRepository
                    .findByTransactionUuid(uuid)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // 🔥 Update status
            if ("COMPLETE".equals(statusResponse.getStatus())) {
                payment.setStatus(PaymentStatus.SUCCESS);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }

            payment.setTransaction_code(statusResponse.getRef_id());
            campaignPaymentRepository.save(payment);
            String returnUrl = "http://localhost:5173/campaign/" + payment.getCampaign().getId();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", returnUrl)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Payment verification failed");
        }
    }





    @GetMapping("/esewa/failure")
    public ResponseEntity<?> esewaFailure(@RequestParam String returnUrl) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", returnUrl)
                .build();
//        return ResponseEntity.ok("Payment failed or cancelled");
    }



    // -----------------------------
    // Helper: extract user from token
    // -----------------------------
    @GetMapping("/esewa/status")
    public ResponseEntity<?> checkStatus(
    ) {
        String url = String.format(
                "https://rc.esewa.com.np/api/epay/transaction/status/?product_code=EPAYTEST&total_amount=500&transaction_uuid=981e8e19-df0c-40de-9638-d93c775081ff"        );


        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }



}
