package com.example.nepalhandsbackend.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatMessage {
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
}