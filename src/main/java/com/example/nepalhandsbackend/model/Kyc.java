package com.example.nepalhandsbackend.model;

import com.example.nepalhandsbackend.states.KycStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Personal Information ─────────────────────────────

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String fullName;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank
    @Column(nullable = false)
    private String gender;

    // ── Citizenship Information ─────────────────────────

    @NotBlank
    @Column(nullable = false, unique = true)
    private String citizenshipNumber;

    @NotBlank
    @Column(nullable = false)
    private String citizenshipIssuedDistrict;

    @NotNull
    @Column(nullable = false)
    private LocalDate citizenshipIssuedDate;

    @Column(length = 9)
    private String panNumber;

    // ── Contact Information ─────────────────────────────

    @NotBlank
    @Pattern(regexp = "^(\\+977)?9\\d{9}$")
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    // ── Address Information ─────────────────────────────

    @NotBlank
    @Column(nullable = false)
    private String province;

    @NotBlank
    @Column(nullable = false)
    private String district;

    @NotBlank
    @Column(nullable = false)
    private String municipality;

    @NotBlank
    @Column(nullable = false)
    private String wardNumber;

    private String tole;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(columnDefinition = "TEXT")
    private String temporaryAddress;

    // ── Occupation & Funds ──────────────────────────────

    @NotBlank
    @Column(nullable = false)
    private String occupation;

    @NotBlank
    @Column(nullable = false)
    private String sourceOfFunds;

    // ── Documents ───────────────────────────────────────

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] citizenshipFront;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] citizenshipBack;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] panDocument;

    // ── Status ──────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private KycStatus status = KycStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    // ── Metadata ────────────────────────────────────────

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}