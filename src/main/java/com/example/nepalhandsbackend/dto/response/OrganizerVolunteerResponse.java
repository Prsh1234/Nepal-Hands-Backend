package com.example.nepalhandsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrganizerVolunteerResponse {

    private Long id;
    private String title;
    private String category;
    private String status;

    private Integer capacity;
    private Integer accepted;
    private Integer applicants;

    private String location;
    private LocalDate startDate;
}