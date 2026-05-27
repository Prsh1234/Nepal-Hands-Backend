package com.example.nepalhandsbackend.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CampaignRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be 100 characters or fewer")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    @NotBlank(message = "LongDescription is required")
    @Size(min = 40, message = "LongDescription must be at least 40 characters")
    private String longDescription;

    @NotBlank(message = "Project Scope are required")
    private String projectScope;

    @Min(value = 1, message = "Must be at least 1")
    private Long goal;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotBlank(message = "Organizer is required")
    private String organizer;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email address")
    private String contactEmail;

    private String contactPhone;

    private MultipartFile coverImage;
    private List<MultipartFile> images;

    // verification

    @NotBlank(message = "Organization legal name is required")
    private String orgLegalName;

    @NotBlank(message = "Organization type is required")
    private String orgType;

    @NotBlank(message = "Registration number is required")
    private String regNumber;

    @NotBlank(message = "Registering authority is required")
    private String regAuthority;

    private LocalDate regDate;

    @NotBlank(message = "PAN number is required")
    private String panNumber;

    private String website;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Bank Account Holder Name is required")
    private String bankAccountHolderName;

    @NotBlank(message = "Bank Account Number is required")
    private String bankAccountNumber;

    @NotBlank(message = "Authorized signatory is required")
    private String authorizedSignatory;

    @NotBlank(message = "Designation is required")
    private String signatoryRole;


    // uploaded documents
    private List<MultipartFile> documents;

    // registration, swc, citizenship, pan...
    private List<String> documentTypes;
}
