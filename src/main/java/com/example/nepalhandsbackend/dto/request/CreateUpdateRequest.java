package com.example.nepalhandsbackend.dto.request;

import lombok.Data;

@Data
public class CreateUpdateRequest {
    private Long id;
    private String title;
    private String body;
}