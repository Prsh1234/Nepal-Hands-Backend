package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.EsewaPaymentRequest;
import com.example.nepalhandsbackend.dto.response.EsewaSignatureResponse;
import com.example.nepalhandsbackend.dto.response.EsewaStatusResponse;
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
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final String secretKey = "8gBm/:&EnhH.1/q";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignPaymentRepository campaignPaymentRepository;
    @Autowired
    private EsewaVerificationService verificationService;
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
            campaignPayment.setAnonymous(paymentRequest.isAnonymous());
            System.out.println(paymentRequest.isAnonymous());

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


    public CampaignPayment processPayment(String data) throws Exception {

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

        EsewaStatusResponse statusResponse =
                verificationService.checkStatus(productCode, uuid, total);

        CampaignPayment payment = campaignPaymentRepository
                .findByTransactionUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(
                "COMPLETE".equals(statusResponse.getStatus())
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED
        );

        payment.setTransaction_code(statusResponse.getRef_id());

        return campaignPaymentRepository.save(payment);
    }
}
