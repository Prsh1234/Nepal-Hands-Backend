package com.example.nepalhandsbackend.dto.request;

import lombok.Data;

@Data
public class EsewaPaymentRequest {
    private int total_amount;
    private String product_code;
    private Long campaignId;
    private boolean anonymous;
}
