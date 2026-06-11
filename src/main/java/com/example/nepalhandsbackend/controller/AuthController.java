package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.request.LoginRequest;
import com.example.nepalhandsbackend.dto.request.RefreshTokenRequest;
import com.example.nepalhandsbackend.dto.request.SignupRequest;
import com.example.nepalhandsbackend.dto.response.AuthResponse;
import com.example.nepalhandsbackend.dto.response.UserResponse;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.Role;
import com.example.nepalhandsbackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ─── Sign Up ──────────────────────────────────────────────────────────────

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already registered"));
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(Set.of(Role.ROLE_VOLUNTEER));

        userRepository.save(user);

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse(accessToken, refreshToken, toUserResponse(user)));
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        Optional<User> optional = userRepository.findByEmail(req.getEmail());

        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = optional.get();

        if (user.getPassword() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "This account uses Google login. Please sign in with Google."));
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, toUserResponse(user)));
    }

    // ─── Refresh Token ────────────────────────────────────────────────────────

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest req) {

        String refreshToken = req.getRefreshToken();

        if (refreshToken == null
                || !jwtUtil.validateToken(refreshToken)
                || !"refresh".equals(jwtUtil.extractType(refreshToken))) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired refresh token"));
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRoles());

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // ─── Me ───────────────────────────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(toUserResponse(user));
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .profilePic(user.getProfilePic())
                .build();
    }
}