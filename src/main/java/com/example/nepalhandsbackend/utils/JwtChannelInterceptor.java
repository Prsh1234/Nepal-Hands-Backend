package com.example.nepalhandsbackend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        System.out.println("STOMP COMMAND: " + accessor.getCommand());


        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("❌ Missing Authorization header");
                return message;
            }

            String token = authHeader.substring(7);

            Authentication authentication =
                    jwtService.getAuthentication(token);
            if (authentication == null) {
                System.out.println("❌ Not authenticated user");
                return message;
            }
            accessor.setUser(authentication);

            System.out.println("✅ WebSocket Authenticated: " + authentication.getName());
        }

        return message;
    }
}