package com.example.difme.dto.application;

import java.time.LocalDateTime;

import com.example.difme.dto.freelancer.FreelancerResponseDto;
import com.example.difme.dto.project.ProjectResponseDto;
import com.example.difme.model.enums.ApplicationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Application response data")
public class ApplicationResponseDto {

    @Schema(description = "Application ID", example = "1")
    private Long id;

    @Schema(description = "Cover letter or application message", example = "I am excited to apply for this project. With 5 years of experience in web development...")
    private String coverLetter;

    @Schema(description = "Application status", example = "PENDING")
    private ApplicationStatus status;

    @Schema(description = "Application submission date", example = "2024-01-15T10:30:00")
    private LocalDateTime appliedAt;

    @Schema(description = "Last update date", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Freelancer who submitted the application")
    private FreelancerResponseDto freelancer;

    @Schema(description = "Project the application was submitted for")
    private ProjectResponseDto project;
}
