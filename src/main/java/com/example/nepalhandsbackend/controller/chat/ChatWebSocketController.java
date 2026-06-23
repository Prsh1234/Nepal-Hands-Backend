package com.example.nepalhandsbackend.controller.chat;

import com.example.nepalhandsbackend.dto.request.ChatMessage;
import com.example.nepalhandsbackend.dto.request.VolunteerChatRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerChatResponse;
import com.example.nepalhandsbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/private-message")
    public void sendPrivateMessage(ChatMessage message) {

        System.out.println("sending message");

        messagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(), // ✅ NOW ID
                "/queue/messages",
                message
        );
    }




    @MessageMapping("/group.send")
    public void sendToGroup(VolunteerChatRequest message) throws IOException {
        VolunteerChatResponse saved = chatService.saveChat(message);

        messagingTemplate.convertAndSend(
                "/topic/room." + saved.getOpportunityId(),
                saved
        );
    }

}



