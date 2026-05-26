package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volunteer_verification_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerVerificationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // registration | swc | citizenship | pan | tax | constitution | permission
    @Column(nullable = false)
    private String documentType;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] file;

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String contentType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_id")
    private VolunteerOpportunityVerification verification;
}