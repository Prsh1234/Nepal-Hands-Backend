package com.example.nepalhandsbackend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CampaignSelectResponse {
    private Long id;
    private String title;
}
