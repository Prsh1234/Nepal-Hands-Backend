package com.example.nepalhandsbackend.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String redirectUrl =
                "http://localhost:5173/oauth2/redirect"
                        + "?error="
                        + URLEncoder.encode(
                        exception.getMessage(),
                        StandardCharsets.UTF_8
                );

        getRedirectStrategy().sendRedirect(
                request,
                response,
                redirectUrl
        );
    }
}