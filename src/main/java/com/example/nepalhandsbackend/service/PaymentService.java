package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.EsewaPaymentRequest;
import com.example.nepalhandsbackend.dto.response.EsewaSignatureResponse;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class PaymentService {
    private final String secretKey = "8gBm/:&EnhH.1/q";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignPaymentRepository campaignPaymentRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    public EsewaSignatureResponse generateSignature(EsewaPaymentRequest paymentRequest) {
        String transaction_uuid = UUID.randomUUID().toString();

        String message =
                "total_amount=" + paymentRequest.getTotal_amount() +
                        ",transaction_uuid=" + transaction_uuid +
                        ",product_code=" + paymentRequest.getProduct_code();
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            String signature = Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));

            CampaignPayment campaignPayment = new CampaignPayment();
            campaignPayment.setAmount(paymentRequest.getTotal_amount());
            campaignPayment.setTransactionUuid(transaction_uuid);
            campaignPayment.setProduct_code(paymentRequest.getProduct_code());
            campaignPayment.setStatus(PaymentStatus.PENDING);
            campaignPayment.setTransaction_code(null);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                campaignPayment.setUser(user);
            }
            Campaign campaign = campaignRepository.findById(paymentRequest.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));
            campaignPayment.setCampaign(campaign);
            campaignPaymentRepository.save(campaignPayment);
            System.out.println(signature);

            return EsewaSignatureResponse.builder()
                    .signature(signature)
                    .signed_field_names("total_amount,transaction_uuid,product_code")
                    .transaction_uuid(transaction_uuid)
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Payment signature generation failed", e);
        }

    }
}
