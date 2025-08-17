package com.example.difme.dto.project;

import java.time.LocalDateTime;
import java.util.List;

import com.example.difme.dto.employer.EmployerResponseDto;
import com.example.difme.dto.skill.SkillResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project response data")
public class ProjectResponseDto {

    @Schema(description = "Project ID", example = "1")
    private Long id;

    @Schema(description = "Project title", example = "E-commerce Website Development")
    private String projectName;

    @Schema(description = "Detailed project description", example = "Build a modern e-commerce platform with React and Spring Boot")
    private String projectDescription;

    @Schema(description = "Project start date", example = "2024-02-01T09:00:00")
    private LocalDateTime projectStart;

    @Schema(description = "Project deadline", example = "2024-06-01T18:00:00")
    private LocalDateTime projectDeadline;

    @Schema(description = "Project creation date", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Employer who posted the project")
    private EmployerResponseDto employer;

    @Schema(description = "Required skills for the project")
    private List<SkillResponseDto> requiredSkills;
}
