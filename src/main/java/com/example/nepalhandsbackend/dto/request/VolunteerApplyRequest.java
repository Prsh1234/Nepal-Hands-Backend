package com.example.nepalhandsbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VolunteerApplyRequest {

    @NotBlank(message = "Phone Number is required")
    private String phone;

    private String motivation;

}
