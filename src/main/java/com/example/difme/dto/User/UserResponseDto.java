package com.example.difme.dto.User;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dto for user response data")
public record UserResponseDto(
        @Schema(description = "Username", example = "johndoe") @NotBlank String userName,

        @Schema(description = "First name", example = "John") @NotBlank String firstName,

        @Schema(description = "Last name", example = "Doe") @NotBlank String lastName,

        @Schema(description = "Creation date", example = "2025-07-29T12:30:00") @NotBlank LocalDateTime creationDate,

        @Schema(description = "Email address", example = "john.doe@example.com") @Email String email) {

}
