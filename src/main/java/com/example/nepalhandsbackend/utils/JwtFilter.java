package com.example.nepalhandsbackend.utils;

import com.example.nepalhandsbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth/") || path.startsWith("/oauth2/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token) && "access".equals(jwtUtil.extractType(token))) {

                String email = jwtUtil.extractEmail(token);
                List<String> roles = jwtUtil.extractRoles(token);


                if (roles != null) {
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> {
                                String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                                return new SimpleGrantedAuthority(authority);
                            })
                            .collect(Collectors.toList());

                    System.out.println("=== Final authorities set on context: " + authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("=== Authentication set successfully for: " + email);
                } else {
                    System.out.println("=== WARNING: roles list is null, no authentication set");
                }
            } else {
                System.out.println("=== WARNING: Token invalid or not access type — no authentication set");
            }
        } else {
            System.out.println("=== WARNING: No Bearer token found in request");
        }

        filterChain.doFilter(request, response);
    }
}