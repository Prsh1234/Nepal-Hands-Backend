package com.example.nepalhandsbackend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonorResponse {
    private Integer donorId;
    private String donorName;
    private Double amount;
    private LocalDateTime donatedAt;
}
