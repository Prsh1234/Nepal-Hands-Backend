package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class VolunteerApplyResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String motivation;
    private Long opportunityId;
    private String opportunityTitle;
    private int volunteerId;
    private List<String> skills;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
    private ApplicationStatus status;
}
