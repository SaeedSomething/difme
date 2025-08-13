package com.example.difme.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Dto for user creation requests")
public record UserCreationRequestDto(
                @Schema(description = "Username", example = "johndoe") @NotBlank String userName,

                @Schema(description = "Hashed password", example = "hashedpassword123") @NotBlank String password,

                @Schema(description = "First name", example = "John") @NotBlank String firstName,

                @Schema(description = "Last name", example = "Doe") @NotBlank String lastName,

                @Schema(description = "Email address", example = "john.doe@example.com") @Email String email) {
}