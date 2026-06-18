package com.example.nepalhandsbackend.controller.campaignPayment;

import com.example.nepalhandsbackend.dto.request.EsewaPaymentRequest;
import com.example.nepalhandsbackend.dto.response.EsewaSignatureResponse;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.service.EsewaVerificationService;
import com.example.nepalhandsbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/esewa/campaign")
public class EsewaController {


    @Autowired
    private PaymentService paymentService;




    @GetMapping("/success")
    public ResponseEntity<?> esewaSuccess(@RequestParam String data) {

        try {
            CampaignPayment payment = paymentService.processPayment(data);
            String returnUrl = "http://localhost:5173/campaign/" + payment.getCampaign().getId();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", returnUrl)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Payment verification success");
        }
    }





    @GetMapping("/failure")
    public ResponseEntity<?> esewaFailure(@RequestParam String data) {

        try {
            CampaignPayment payment = paymentService.processPayment(data);
            String returnUrl = "http://localhost:5173/campaign/" + payment.getCampaign().getId();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", returnUrl)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Payment verification failed");
        }
    }




}
