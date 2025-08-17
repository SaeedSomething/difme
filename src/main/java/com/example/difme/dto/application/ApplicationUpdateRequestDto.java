package com.example.difme.dto.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an application")
public class ApplicationUpdateRequestDto {

    @NotBlank(message = "Cover letter is required")
    @Schema(description = "Updated cover letter or application message", example = "I am excited to apply for this project. With 5 years of experience in web development...")
    private String coverLetter;
}
