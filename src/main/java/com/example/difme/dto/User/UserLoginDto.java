package com.example.difme.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dto for user login requests")
public record UserLoginDto(@Schema(description = "Username", example = "johndoe") @NotBlank String userName,
        @Schema(description = "Hashed password", example = "hashedpassword123") @NotBlank String password) {

}
