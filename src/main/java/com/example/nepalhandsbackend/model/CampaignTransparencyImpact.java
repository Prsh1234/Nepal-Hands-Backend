package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;


@Entity
@Table(name = "campaign_transparency_impact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignTransparencyImpact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "campaign_id",
            nullable = false)
    private Campaign campaign;

    @Column(nullable = false)
    private String type;
    //file
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String contentType;
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] file;
    @CreationTimestamp
    private LocalDate uploadedAt;

}
