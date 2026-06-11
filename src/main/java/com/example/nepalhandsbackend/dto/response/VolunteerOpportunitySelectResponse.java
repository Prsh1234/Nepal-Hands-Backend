package com.example.nepalhandsbackend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VolunteerOpportunitySelectResponse {
    private Long id;
    private String title;
}
