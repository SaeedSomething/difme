package com.example.difme.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.difme.dto.application.ApplicationCreateRequestDto;
import com.example.difme.dto.application.ApplicationCreateWithIdsRequestDto;
import com.example.difme.dto.application.ApplicationResponseDto;
import com.example.difme.dto.application.ApplicationUpdateRequestDto;
import com.example.difme.model.ApplicationModel;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.ProjectModel;

/**
 * Factory class for converting between Application DTOs and ApplicationModel
 */
@Component
public class ApplicationDtoFactory {

    @Autowired
    private FreelancerDtoFactory freelancerDtoFactory;

    @Autowired
    private ProjectDtoFactory projectDtoFactory;

    /**
     * Convert ApplicationCreateRequestDto to ApplicationModel
     */
    public ApplicationModel toModel(ApplicationCreateRequestDto dto, FreelancerModel freelancer, ProjectModel project) {
        if (dto == null)
            return null;

        return ApplicationModel.builder()
                .coverLetter(dto.getCoverLetter())
                .freelancer(freelancer)
                .project(project)
                .build();
    }

    /**
     * Convert ApplicationCreateWithIdsRequestDto to ApplicationModel
     */
    public ApplicationModel toModel(ApplicationCreateWithIdsRequestDto dto, FreelancerModel freelancer,
            ProjectModel project) {
        if (dto == null)
            return null;

        return ApplicationModel.builder()
                .coverLetter(dto.getCoverLetter())
                .freelancer(freelancer)
                .project(project)
                .build();
    }

    /**
     * Convert ApplicationModel to ApplicationResponseDto
     */
    public ApplicationResponseDto toResponseDto(ApplicationModel model) {
        if (model == null)
            return null;

        return new ApplicationResponseDto(
                model.getId(),
                model.getCoverLetter(),
                model.getStatus(),
                model.getAppliedAt(),
                model.getUpdatedAt(),
                freelancerDtoFactory.toResponseDto(model.getFreelancer()),
                projectDtoFactory.toResponseDto(model.getProject()));
    }

    /**
     * Update ApplicationModel with data from ApplicationUpdateRequestDto
     */
    public ApplicationModel updateModel(ApplicationModel model, ApplicationUpdateRequestDto dto) {
        if (model == null || dto == null)
            return model;

        if (dto.getCoverLetter() != null) {
            model.setCoverLetter(dto.getCoverLetter());
        }

        return model;
    }

    /**
     * Convert a list of ApplicationModels to ApplicationResponseDtos
     */
    public List<ApplicationResponseDto> toResponseDtoList(List<ApplicationModel> models) {
        if (models == null)
            return null;
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
