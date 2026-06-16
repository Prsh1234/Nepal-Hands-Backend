package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.response.EsewaStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EsewaVerificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public EsewaStatusResponse checkStatus(String productCode, String uuid, double totalAmount) {

        String url = String.format(
                "https://rc.esewa.com.np/api/epay/transaction/status/" +
                        "?product_code=%s&transaction_uuid=%s&total_amount=%s",
                productCode, uuid, totalAmount
        );

        return restTemplate.getForObject(url, EsewaStatusResponse.class);
    }
}
