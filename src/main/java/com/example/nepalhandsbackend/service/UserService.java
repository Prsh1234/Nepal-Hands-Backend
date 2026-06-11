package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.UserUpdateRequest;
import com.example.nepalhandsbackend.dto.response.UserResponse;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.KycRepository;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.KycStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse getProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .profilePic(user.getProfilePic())
                .bio(user.getBio())
                .location(user.getLocation())
                .skills(user.getSkills())
                .causes(user.getCauses())
                .joinedAt(
                        user.getJoinedAt() != null
                                ? user.getJoinedAt()
                                : LocalDateTime.now())
                .kycStatus(
                        user.getKyc() != null
                                ? user.getKyc().getStatus()
                                : KycStatus.NOT_APPLIED
                )
                .build();
    }
    public UserResponse updateProfile(
            Integer userId,
            UserUpdateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBio(request.getBio());
        user.setLocation(request.getLocation());

        if (request.getProfilePic() != null) {
            user.setProfilePic(request.getProfilePic());
        }

        user.setSkills(
                request.getSkills() != null
                        ? request.getSkills()
                        : List.of()
        );

        user.setCauses(
                request.getCauses() != null
                        ? request.getCauses()
                        : List.of()
        );

        userRepository.save(user);

        return getProfile(userId);
    }


    public UserResponse updateProfilePicture(
            Integer userId,
            MultipartFile file
    ) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setProfilePic(file.getBytes());

        userRepository.save(user);

        return getProfile(userId);
    }
}
