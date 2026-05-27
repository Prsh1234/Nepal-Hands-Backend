package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaign_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "campaign_id",
            nullable = false,
            unique = true)
    private Campaign campaign;

    // organization

    @NotBlank
    @Column(nullable = false)
    private String orgLegalName;

    @NotBlank
    @Column(nullable = false)
    private String orgType;


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
    @Column(nullable = false)
    private String authorizedSignatory;

    @NotBlank
    @Column(nullable = false)
    private String signatoryRole;

    @NotBlank
    @Column(nullable = false)
    private String bankName;

    @NotBlank
    @Column(nullable = false)
    private String bankAccountHolderName;

    @NotBlank
    @Column(nullable = false)
    private String bankAccountNumber;



    @OneToMany(
            mappedBy = "verification",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CampaignVerificationDocument> documents = new ArrayList<>();
}