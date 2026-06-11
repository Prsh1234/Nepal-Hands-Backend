package com.example.nepalhandsbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class VolunteerOpportunityRequest {

    // Step 0 – Details
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be 100 characters or fewer")
    private String title;

    @NotBlank(message = "Category is required")
    @Pattern(
            regexp = "teaching|healthcare|construction|environment|water|community",
            message = "Invalid category"
    )
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    @NotBlank(message = "LongDescription is required")
    @Size(min = 40, message = "LongDescription must be at least 40 characters")
    private String longDescription;

    // Step 1 – Requirements
    @NotEmpty(message = "At least one skill is required")
    private List<String> requiredSkills;

    @Min(value = 1, message = "Must have at least 1 volunteer spot")
    @Max(value = 500, message = "Cannot exceed 500 spots")
    private Integer volunteerSpots;

    @Min(value = 16, message = "Minimum age is 16")
    @Max(value = 65, message = "Maximum age cap is 65")
    private Integer minimumAge = 18;

    @NotBlank(message = "Commitment type is required")
    @Pattern(
            regexp = "one-time|short-term|medium-term|long-term",
            message = "Invalid commitment type"
    )
    private String commitmentType;

    @NotBlank(message = "Requirements are required")
    private String requirements;

    // Step 2 – Activities & Impact
    @NotBlank(message = "Activities are required")
    private String activities;

    @NotBlank(message = "Why it matters is required")
    @Size(min = 20, message = "Why it matters must be at least 20 characters")
    private String whyItMatters;

    @NotBlank(message = "Benefits are required")
    private String benefits;

    // Step 3 – Schedule & Contact
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Min(value = 1)
    @Max(value = 24)
    private Integer dailyHours = 6;


    private MultipartFile coverImage;
    private List<MultipartFile> images;

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email address")
    private String contactEmail;

    private String contactPhone;


    // verification

    @NotBlank(message = "Organization legal name is required")
    private String orgLegalName;

    @NotBlank(message = "Organization type is required")
    private String orgType;

    @NotBlank(message = "Organization address is required")
    private String orgAddress;

    @NotBlank(message = "Registration number is required")
    private String regNumber;

    @NotBlank(message = "Registering authority is required")
    private String regAuthority;

    private LocalDate regDate;

    @NotBlank(message = "PAN number is required")
    private String panNumber;

    private String website;

    @NotBlank(message = "Official email is required")
    @Email
    private String officialEmail;

    @NotBlank(message = "Official phone is required")
    private String officialPhone;

    @NotBlank(message = "Authorized signatory is required")
    private String authorizedSignatory;

    @NotBlank(message = "Designation is required")
    private String signatoryRole;


    // uploaded documents
    private List<MultipartFile> documents;

    // registration, swc, citizenship, pan...
    private List<String> documentTypes;
}
