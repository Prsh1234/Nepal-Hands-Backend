package com.example.nepalhandsbackend.dto.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VolunteerChatResponse {
    private Long id;
    private Long opportunityId;
    private Integer senderId;
    private String senderName;
    private String content;
    private Instant sentAt;
    private String file;       // base64 for frontend
    private String fileType;
    private String fileName;


}
