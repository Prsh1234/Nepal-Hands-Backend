package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.KycStatus;
import com.example.nepalhandsbackend.states.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private String bio;
    private String location;
    private byte[] profilePic;
    private List<String> skills;
    private List<String> causes;
    private KycStatus kycStatus;
    private LocalDateTime joinedAt;

}