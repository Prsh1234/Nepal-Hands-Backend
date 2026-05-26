package com.example.nepalhandsbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class KycRequest {

    // ── Personal Information ─────────────────────────────

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name must be 120 characters or fewer")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    // ── Citizenship Information ─────────────────────────

    @NotBlank(message = "Citizenship number is required")
    private String citizenshipNumber;

    @NotBlank(message = "Citizenship issued district is required")
    private String citizenshipIssuedDistrict;

    @NotNull(message = "Citizenship issued date is required")
    private LocalDate citizenshipIssuedDate;

    @Pattern(
            regexp = "^\\d{9}$",
            message = "PAN number must be 9 digits"
    )
    private String panNumber;

    // ── Contact Information ─────────────────────────────

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+977)?9\\d{9}$",
            message = "Invalid Nepali phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    // ── Address Information ─────────────────────────────

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Municipality is required")
    private String municipality;

    @NotBlank(message = "Ward number is required")
    private String wardNumber;

    private String tole;

    @NotBlank(message = "Permanent address is required")
    private String permanentAddress;

    private String temporaryAddress;

    // ── Occupation & Funds ──────────────────────────────

    @NotBlank(message = "Occupation is required")
    private String occupation;

    @NotBlank(message = "Source of funds is required")
    private String sourceOfFunds;

    // ── Documents ───────────────────────────────────────

    @NotNull(message = "Citizenship front image is required")
    private MultipartFile citizenshipFront;

    @NotNull(message = "Citizenship back image is required")
    private MultipartFile citizenshipBack;

    private MultipartFile panDocument;
}