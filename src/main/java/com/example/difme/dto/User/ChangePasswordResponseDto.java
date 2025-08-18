package com.example.difme.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Response for password change operation")
public record ChangePasswordResponseDto(
                @Schema(description = "Success message") String message,

                @Schema(description = "Timestamp when password was changed") LocalDateTime changedAt,

                @Schema(description = "Whether user should re-authenticate") boolean requiresReAuthentication) {
}