package com.example.difme.dto.employer;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employer response data")
public class EmployerResponseDto {

    @Schema(description = "Employer ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "tech_company")
    private String userName;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Smith")
    private String lastName;

    @Schema(description = "Email address", example = "john.smith@company.com")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Company description", example = "Leading tech company specializing in web development")
    private String companyDescription;

}
