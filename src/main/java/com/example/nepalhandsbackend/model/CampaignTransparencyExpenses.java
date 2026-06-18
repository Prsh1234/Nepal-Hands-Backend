package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campaign_transparency_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignTransparencyExpenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "campaign_id",
            nullable = false)
    private Campaign campaign;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String vendor;

    @Column(nullable = false)
    private Double amount;
    //file
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String contentType;
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] file;

    private LocalDate date;

}
