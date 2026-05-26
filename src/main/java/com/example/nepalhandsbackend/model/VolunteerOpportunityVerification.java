package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volunteer_opportunity_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerOpportunityVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false, unique = true)
    private VolunteerOpportunity opportunity;

    // organization

    @NotBlank
    @Column(nullable = false)
    private String orgLegalName;

    @NotBlank
    @Column(nullable = false)
    private String orgType;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String orgAddress;

    @NotBlank
    @Column(nullable = false)
    private String regNumber;

    @NotBlank
    @Column(nullable = false)
    private String regAuthority;

    private LocalDate regDate;

    @NotBlank
    @Column(nullable = false)
    private String panNumber;

    private String website;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String officialEmail;

    @NotBlank
    @Column(nullable = false)
    private String officialPhone;

    @NotBlank
    @Column(nullable = false)
    private String authorizedSignatory;

    @NotBlank
    @Column(nullable = false)
    private String signatoryRole;

    @OneToMany(
            mappedBy = "verification",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<VolunteerVerificationDocument> documents = new ArrayList<>();
}