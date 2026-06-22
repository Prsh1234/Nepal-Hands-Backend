package com.example.nepalhandsbackend.utils;

import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my_super_secret_key_that_is_at_least_32_chars!";
    private final long ACCESS_TOKEN_EXP  = 1000L * 60 * 60*24;           // 1 hour
    private final long REFRESH_TOKEN_EXP = 1000L * 60 * 60 * 24 * 30; // 30 days

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    @Autowired
    private UserRepository userRepository;
    // ─── Token Generation ─────────────────────────────────────────────────────

    public String generateAccessToken(String email, Set<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(email)
                .claim("type", "access")
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ─── Token Extraction ─────────────────────────────────────────────────────

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public Integer extractUserId(String token) {
        String email = extractEmail(token);

        Integer userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
        return userId;
    }
    public String extractType(String token) {
        return getClaims(token).get("type", String.class);
    }

    public List<String> extractRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    // ─── Validation ───────────────────────────────────────────────────────────

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    // ─── Internal ─────────────────────────────────────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Authentication getAuthentication(String token) {

        String email = extractEmail(token);

        Integer userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
        if (!validateToken(token)) {
            return null;
        }
        List<SimpleGrantedAuthority> authorities =
                extractRoles(token).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                userId.toString(),   // ✅ IMPORTANT CHANGE
                null,
                authorities
        );
    }
}