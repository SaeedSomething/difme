package com.example.difme.dto.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new application")
public class ApplicationCreateRequestDto {

    @NotNull(message = "Project ID is required")
    @Schema(description = "ID of the project to apply to", example = "1")
    private Long projectId;

    @NotBlank(message = "Cover letter is required")
    @Schema(description = "Cover letter or application message", example = "I am excited to apply for this project. With 5 years of experience in web development...")
    private String coverLetter;
}
