package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.VolunteerCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VolunteerCardDTO {
    private Long id;
    private String title;
    private String description;
    private String longDescription;
    private String organizer;
    private VolunteerCategory category;
    private LocalDateTime postedAt;
    private List<String> requiredSkills;
    private String location;
    private Integer totalSpots;
    private Long filledSpots;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer dailyHours;
    private String commitmentType;
}
