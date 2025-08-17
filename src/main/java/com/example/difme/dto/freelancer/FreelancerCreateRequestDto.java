package com.example.difme.dto.freelancer;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new freelancer")
public class FreelancerCreateRequestDto {

    @NotBlank(message = "Username is required")
    @Schema(description = "Unique username", example = "john_developer")
    private String userName;

    @NotBlank(message = "First name is required")
    @Schema(description = "Freelancer's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Freelancer's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Schema(description = "Freelancer's password", example = "password123")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address", example = "john.doe@email.com")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

}
