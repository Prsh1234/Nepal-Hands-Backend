package com.example.nepalhandsbackend.controller.chat;

import com.example.nepalhandsbackend.dto.request.VolunteerChatRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerChatResponse;
import com.example.nepalhandsbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatFileController {

    private final ChatService chatService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VolunteerChatResponse> uploadFile(
            @RequestParam("file")          MultipartFile file,
            @RequestParam("opportunityId") Long opportunityId,
            @RequestParam("senderId")      Integer senderId,
            @RequestParam("senderName")    String senderName) throws IOException {
        VolunteerChatRequest req = new VolunteerChatRequest(
                opportunityId, senderId, senderName,
                null,
                Instant.now(),
                file,
                file.getContentType()
        );
        VolunteerChatResponse saved = chatService.saveChat(req);
        chatService.broadcast(saved);
        return ResponseEntity.ok(saved);
    }
}
