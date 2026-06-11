package com.example.nepalhandsbackend.controller;

import com.example.nepalhandsbackend.dto.request.UserUpdateRequest;
import com.example.nepalhandsbackend.dto.response.CampaignResponse;
import com.example.nepalhandsbackend.dto.response.CampaignUpdateResponse;
import com.example.nepalhandsbackend.dto.response.UserResponse;
import com.example.nepalhandsbackend.dto.response.VolunteerApplyResponse;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.service.UserService;
import com.example.nepalhandsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public ResponseEntity<?> getBasicUserData(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Integer id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }
    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserUpdateRequest request) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                userService.updateProfile(userId, request)
        );
    }
    @PutMapping(
            value = "/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponse> updateProfilePicture(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("profilePic") MultipartFile profilePic
    ) throws IOException {

        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                userService.updateProfilePicture(
                        userId,
                        profilePic
                )
        );
    }

}
