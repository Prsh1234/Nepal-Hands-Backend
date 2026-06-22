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
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor

public class ChatService {
    private final UserRepository userRepo;
    private final VolunteerOpportunityRepository opportunityRepo;
    private final VolunteerChatRepository chatRepository;

    public VolunteerChatResponse saveChat(VolunteerChatRequest message){
        User sender = userRepo.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. fetch room
        VolunteerOpportunity opportunity = opportunityRepo.findById(message.getOpportunityId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3. save to DB
        VolunteerChat entity = VolunteerChat.builder()
                .opportunity(opportunity)
                .sender(sender)
                .senderName(message.getSenderName())
                .content(message.getContent())
                .sentAt(message.getSentAt() != null ? message.getSentAt() : Instant.now())
                .build();

        VolunteerChat saved =  chatRepository.save(entity);
        return VolunteerChatResponse.builder()
                .id(saved.getId())
                .opportunityId(opportunity.getId())
                .senderId(sender.getId())
                .senderName(saved.getSenderName())
                .content(saved.getContent())
                .sentAt(saved.getSentAt())
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
                .build();
    }
}

