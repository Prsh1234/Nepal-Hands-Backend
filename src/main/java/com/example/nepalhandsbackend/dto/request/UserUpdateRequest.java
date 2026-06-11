package com.example.nepalhandsbackend.dto.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private byte[] profilePic;
    private List<String> skills;
    private List<String> causes;
}
