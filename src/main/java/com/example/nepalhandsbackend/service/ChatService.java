package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.VolunteerChatRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerChatResponse;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.model.VolunteerChat;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.repository.VolunteerChatRepository;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor

public class ChatService {
    private final UserRepository userRepo;
    private final VolunteerOpportunityRepository opportunityRepo;
    private final VolunteerChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public VolunteerChatResponse saveChat(VolunteerChatRequest message) throws IOException {
        User sender = userRepo.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. fetch room
        VolunteerOpportunity opportunity = opportunityRepo.findById(message.getOpportunityId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        System.out.println(message.getFile());
        // 3. save to DB
        VolunteerChat entity = VolunteerChat.builder()
                .opportunity(opportunity)
                .sender(sender)
                .senderName(message.getSenderName())
                .content(message.getContent())
                .sentAt(message.getSentAt() != null ? message.getSentAt() : Instant.now())

                .build();
        if(message.getFile() != null){
            entity.setFile(message.getFile().getBytes());
            entity.setFileType(message.getFileType());
            entity.setFileName(message.getFile().getOriginalFilename());
        }
        VolunteerChat saved =  chatRepository.save(entity);
        return VolunteerChatResponse.builder()
                .id(saved.getId())
                .opportunityId(opportunity.getId())
                .senderId(sender.getId())
                .senderName(saved.getSenderName())
                .content(saved.getContent())
                .sentAt(saved.getSentAt())
                .file(saved.getFile() != null          // encode to base64 for frontend
                        ? Base64.getEncoder().encodeToString(saved.getFile())
                        : null)
                .fileType(saved.getFileType())
                .fileName(saved.getFileName())
                .build();
    }
    public Page<VolunteerChatResponse> getMessages(
            Long opportunityId,
            int page,
            int size
    ) {

        return chatRepository
                .findByOpportunityIdOrderBySentAtDesc(
                        opportunityId,
                        PageRequest.of(page, size)
                )
                .map(this::toResponse);
    }

    private VolunteerChatResponse toResponse(VolunteerChat chat) {
        return VolunteerChatResponse.builder()
                .id(chat.getId())
                .opportunityId(chat.getOpportunity().getId())
                .senderId(chat.getSender().getId())
                .senderName(chat.getSenderName())
                .content(chat.getContent())
                .sentAt(chat.getSentAt())
                .file(chat.getFile() != null
                        ? Base64.getEncoder().encodeToString(chat.getFile())
                        : null)
                .fileType(chat.getFileType())
                .fileName(chat.getFileName())
                .build();
    }
    public void broadcast(VolunteerChatResponse msg) {
        messagingTemplate.convertAndSend("/topic/room." + msg.getOpportunityId(), msg);
    }
}

