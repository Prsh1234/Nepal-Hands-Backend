package com.example.nepalhandsbackend.controller.chat;

import com.example.nepalhandsbackend.dto.response.VolunteerChatResponse;
import com.example.nepalhandsbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteer/chat")
@RequiredArgsConstructor
public class VolunteerChatController {

    private final ChatService chatService;
    @GetMapping("/loadChats/{opportunityId}")
    public Page<VolunteerChatResponse> getMessages(
            @PathVariable Long opportunityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        return chatService.getMessages(opportunityId, page, size);
    }
}
