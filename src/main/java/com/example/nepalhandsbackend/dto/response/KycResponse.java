package com.example.nepalhandsbackend.dto.response;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class KycResponse {

    private Long id;
    // ── Personal Information ─────────────────────────────

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
}