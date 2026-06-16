package com.example.nepalhandsbackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsewaStatusResponse {
    private String product_code;
    private String transaction_uuid;
    private double total_amount;
    private String status;
    private String ref_id;
}
