package com.example.difme.dto.employer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new employer")
public class EmployerCreateRequestDto {

    @NotBlank(message = "Username is required")
    @Schema(description = "Unique username", example = "tech_company")
    private String userName;

    @NotBlank(message = "First name is required")
    @Schema(description = "Employer's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Employer's last name", example = "Smith")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address", example = "john.smith@company.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Employer's password", example = "password123")
    private String password;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Company description", example = "Leading tech company specializing in web development")
    private String companyDescription;
}
