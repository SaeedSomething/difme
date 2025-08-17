package com.example.difme.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.difme.dto.project.ProjectCreateRequestDto;
import com.example.difme.dto.project.ProjectCreateWithEmployerRequestDto;
import com.example.difme.dto.project.ProjectResponseDto;
import com.example.difme.dto.project.ProjectUpdateRequestDto;
import com.example.difme.model.EmployerModel;
import com.example.difme.model.ProjectModel;

/**
 * Factory class for converting between Project DTOs and ProjectModel
 */
@Component
public class ProjectDtoFactory {

    @Autowired
    private EmployerDtoFactory employerDtoFactory;

    /**
     * Convert ProjectCreateRequestDto to ProjectModel
     */
    public ProjectModel toModel(ProjectCreateRequestDto dto, EmployerModel employer) {
        if (dto == null)
            return null;

        return ProjectModel.builder()
                .projectName(dto.getProjectName())
                .projectDescription(dto.getProjectDescription())
                .projectStart(dto.getProjectStart())
                .projectDeadline(dto.getProjectDeadline())
                .employer(employer)
                .build();
    }

    /**
     * Convert ProjectCreateWithEmployerRequestDto to ProjectModel
     */
    public ProjectModel toModel(ProjectCreateWithEmployerRequestDto dto, EmployerModel employer) {
        if (dto == null)
            return null;

        return ProjectModel.builder()
                .projectName(dto.getProjectName())
                .projectDescription(dto.getProjectDescription())
                .projectStart(dto.getProjectStart())
                .projectDeadline(dto.getProjectDeadline())
                .employer(employer)
                .build();
    }

    /**
     * Convert ProjectModel to ProjectResponseDto
     */
    public ProjectResponseDto toResponseDto(ProjectModel model) {
        if (model == null)
            return null;

        return new ProjectResponseDto(
                model.getId(),
                model.getProjectName(),
                model.getProjectDescription(),
                model.getProjectStart(),
                model.getProjectDeadline(),
                null, // createdAt - ProjectModel doesn't have this field
                null, // updatedAt - ProjectModel doesn't have this field
                employerDtoFactory.toResponseDto(model.getEmployer()),
                null // requiredSkills - will be handled separately if needed
        );
    }

    /**
     * Update ProjectModel with data from ProjectUpdateRequestDto
     */
    public ProjectModel updateModel(ProjectModel model, ProjectUpdateRequestDto dto) {
        if (model == null || dto == null)
            return model;

        if (dto.getProjectName() != null) {
            model.setProjectName(dto.getProjectName());
        }
        if (dto.getProjectDescription() != null) {
            model.setProjectDescription(dto.getProjectDescription());
        }
        if (dto.getProjectStart() != null) {
            model.setProjectStart(dto.getProjectStart());
        }
        if (dto.getProjectDeadline() != null) {
            model.setProjectDeadline(dto.getProjectDeadline());
        }

        return model;
    }

    /**
     * Convert a list of ProjectModels to ProjectResponseDtos
     */
    public List<ProjectResponseDto> toResponseDtoList(List<ProjectModel> models) {
        if (models == null)
            return null;
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
