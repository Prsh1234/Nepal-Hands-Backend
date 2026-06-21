package com.example.nepalhandsbackend.model;

import com.example.nepalhandsbackend.states.OpportunityStatus;
import com.example.nepalhandsbackend.states.VolunteerCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "volunteer_opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerOpportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Step 0: Details ──────────────────────────────────────────────────────

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VolunteerCategory category; // teaching | healthcare | construction | environment | water | community

    @NotBlank
    @Column(nullable = false)
    private String location;

    @NotBlank
    @Size(min = 20)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Size(min = 40)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @NotBlank
    @Size(min = 40)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String organizer;
    // ── Step 1: Requirements ─────────────────────────────────────────────────

    @ElementCollection
    @CollectionTable(
            name = "volunteer_opportunity_skills",
            joinColumns = @JoinColumn(name = "opportunity_id")
    )
    @Column(name = "skill")
    @Builder.Default
    private List<String> requiredSkills = new ArrayList<>();

    @Min(1)
    @Max(500)
    @Column(nullable = false)
    private Integer volunteerSpots;

    @Min(16)
    @Max(65)
    @Column(nullable = false)
    private Integer minimumAge;

    @NotBlank
    @Column(nullable = false)
    private String commitmentType; // one-time | short-term | medium-term | long-term

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requirements; // newline-separated

    // ── Step 2: Activities & Impact ──────────────────────────────────────────

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String activities; // newline-separated

    @NotBlank
    @Size(min = 20)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String whyItMatters;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String benefits; // newline-separated

    // ── Step 3: Schedule & Contact ───────────────────────────────────────────

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @Min(1)
    @Max(24)
    @Column(nullable = false)
    private Integer dailyHours;

    @NotBlank
    @Column(nullable = false)
    private String contactName;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String contactEmail;

    @Column
    private String contactPhone;

    // media

    @Lob
    @Column(name = "cover_image", columnDefinition = "LONGBLOB")
    private byte[] coverImage;

    // ✅ multiple images
    @ElementCollection
    @CollectionTable(name = "volunteer_opportunity_images", joinColumns = @JoinColumn(name = "opportunity_id"))
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private List<byte[]> images;

    // verification
    @OneToOne(
            mappedBy = "opportunity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private VolunteerOpportunityVerification verification;

    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerOpportunityUpdate> updates = new ArrayList<>();


    @OneToMany(
            mappedBy = "opportunity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VolunteerApplication> applications = new ArrayList<>();
    // ── Metadata ─────────────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OpportunityStatus status = OpportunityStatus.PENDING_REVIEW;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}