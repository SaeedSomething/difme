package com.example.difme.dto.project;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating a project")
public class ProjectUpdateRequestDto {

    @NotBlank(message = "Project name is required")
    @Schema(description = "Project title", example = "E-commerce Website Development")
    private String projectName;

    @NotBlank(message = "Project description is required")
    @Schema(description = "Detailed project description", example = "Build a modern e-commerce platform with React and Spring Boot")
    private String projectDescription;

    @NotNull(message = "Project start date is required")
    @Schema(description = "Project start date", example = "2024-02-01T09:00:00")
    private LocalDateTime projectStart;

    @NotNull(message = "Project deadline is required")
    @Future(message = "Project deadline must be in the future")
    @Schema(description = "Project deadline", example = "2024-06-01T18:00:00")
    private LocalDateTime projectDeadline;

    @Schema(description = "List of required skill IDs", example = "[1, 2, 5]")
    private List<Long> requiredSkillIds;
}
