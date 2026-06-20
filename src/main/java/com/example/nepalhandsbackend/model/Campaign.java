package com.example.nepalhandsbackend.model;

import com.example.nepalhandsbackend.states.CampaignStatus;
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
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Step 0: Basics ─────────────────────────────────────────────

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String category;

    @NotBlank
    @Column(nullable = false)
    private String location;

    // ── Step 1: Story ─────────────────────────────────────────────

    @NotBlank
    @Size(min = 20)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Size(min = 50)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String projectScope; // newline-separated list

    // ── Step 2: Goal ──────────────────────────────────────────────

    @Min(1000)
    @Column(nullable = false)
    private Long goal;

    @NotBlank
    @Column(nullable = false)
    private String duration; // "15", "30", etc.

    @NotBlank
    @Column(nullable = false)
    private String organizer;

    // ── Step 3: Schedule ──────────────────────────────────────────

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    // ── Step 4: Contact ───────────────────────────────────────────

    @NotBlank
    @Column(nullable = false)
    private String contactName;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String contactEmail;

    @Column
    private String contactPhone;

    // ── Media

    @Lob
    @Column(name = "cover_image", columnDefinition = "LONGBLOB")
    private byte[] coverImage;

    // ✅ multiple images
    @ElementCollection
    @CollectionTable(name = "campaign_images", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private List<byte[]> images;


    // verification
    @OneToOne(
            mappedBy = "campaign",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private CampaignVerification verification;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignUpdate> updates = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignPayment> payments;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignTransparencyExpenses> expenses;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignTransparencyImpact> impacts;
    // ── Metadata ──────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CampaignStatus status = CampaignStatus.PENDING_REVIEW;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}