package com.example.difme.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.difme.dto.skill.SkillCreateRequestDto;
import com.example.difme.dto.skill.SkillResponseDto;
import com.example.difme.dto.skill.SkillUpdateRequestDto;
import com.example.difme.model.SkillModel;

/**
 * Factory class for converting between Skill DTOs and SkillModel
 */
@Component
public class SkillDtoFactory {

    /**
     * Convert SkillCreateRequestDto to SkillModel
     */
    public SkillModel toModel(SkillCreateRequestDto dto) {
        if (dto == null)
            return null;

        return SkillModel.builder()
                .skillName(dto.getSkillName())
                .skillLevel(dto.getSkillLevel())
                .build();
    }

    /**
     * Convert SkillModel to SkillResponseDto
     */
    public SkillResponseDto toResponseDto(SkillModel model) {
        if (model == null)
            return null;

        return new SkillResponseDto(
                model.getId(),
                model.getSkillName(),
                model.getSkillLevel());
    }

    /**
     * Update SkillModel with data from SkillUpdateRequestDto
     */
    public SkillModel updateModel(SkillModel model, SkillUpdateRequestDto dto) {
        if (model == null || dto == null)
            return model;

        if (dto.getSkillName() != null) {
            model.setSkillName(dto.getSkillName());
        }
        if (dto.getSkillLevel() != null) {
            model.setSkillLevel(dto.getSkillLevel());
        }

        return model;
    }

    /**
     * Convert a list of SkillModels to SkillResponseDtos
     */
    public List<SkillResponseDto> toResponseDtoList(List<SkillModel> models) {
        if (models == null)
            return null;
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
