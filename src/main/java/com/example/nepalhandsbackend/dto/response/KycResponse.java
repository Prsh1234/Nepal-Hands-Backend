package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.KycStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class KycResponse {

    private Long id;
    private KycStatus status;
    // ── Personal Information ─────────────────────────────
    private Integer userId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;

    // ── Citizenship Information ─────────────────────────

    private String citizenshipNumber;
    private String citizenshipIssuedDistrict;
    private LocalDate citizenshipIssuedDate;
    private String panNumber;

    // ── Contact Information ─────────────────────────────

    private String phoneNumber;
    private String email;

    // ── Address Information ─────────────────────────────

    private String province;
    private String district;
    private String municipality;
    private String wardNumber;
    private String tole;
    private String permanentAddress;
    private String temporaryAddress;

    // ── Occupation & Funds ──────────────────────────────

    private String occupation;
    private String sourceOfFunds;

    // ── Documents ───────────────────────────────────────

    private byte[] citizenshipFront;
    private byte[] citizenshipBack;
    private byte[] panDocument;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}