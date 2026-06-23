package com.example.nepalhandsbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Entity
@Table(name = "volunteer_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private VolunteerOpportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    private String senderName;

    @Column(columnDefinition = "TEXT",nullable = true)
    private String content;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGBLOB")
    private byte[] file;

    @Column(nullable = true)
    private String fileType;
    @Column(nullable = true)
    private String fileName;
    private Instant sentAt;
}