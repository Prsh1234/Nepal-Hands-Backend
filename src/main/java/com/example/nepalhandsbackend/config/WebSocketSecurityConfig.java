package com.example.nepalhandsbackend.config;

import com.example.nepalhandsbackend.utils.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityConfig
        implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor interceptor;

    @Override
    public void configureClientInboundChannel(
            ChannelRegistration registration) {

        registration.interceptors(interceptor);
    }
}
