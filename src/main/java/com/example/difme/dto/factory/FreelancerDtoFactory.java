package com.example.difme.dto.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.difme.dto.freelancer.FreelancerCreateRequestDto;
import com.example.difme.dto.freelancer.FreelancerResponseDto;
import com.example.difme.dto.freelancer.FreelancerUpdateRequestDto;
import com.example.difme.dto.skill.SkillResponseDto;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.SkillModel;
import com.example.difme.model.enums.Role;
import com.example.difme.service.Freelancer.SkillService;

/**
 * Factory class for converting between Freelancer DTOs and FreelancerModel
 */
@Component
public class FreelancerDtoFactory {
    private final PasswordEncoder passwordEncoder;

    public FreelancerDtoFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private SkillDtoFactory skillDtoFactory;

    @Autowired
    private SkillService skillService;

    /**
     * Convert FreelancerCreateRequestDto to FreelancerModel
     */
    public FreelancerModel toModel(FreelancerCreateRequestDto dto) {
        if (dto == null)
            return null;

        FreelancerModel freelancer = new FreelancerModel();
        freelancer.setUserName(dto.getUserName());
        freelancer.setFirstName(dto.getFirstName());
        freelancer.setLastName(dto.getLastName());
        freelancer.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        freelancer.setEmail(dto.getEmail());
        freelancer.setPhoneNumber(dto.getPhoneNumber());
        freelancer.setRole(Role.FREELANCER);
        return freelancer;
    }

    /**
     * Convert FreelancerModel to FreelancerResponseDto
     */
    public FreelancerResponseDto toResponseDto(FreelancerModel model) {
        if (model == null)
            return null;

        List<SkillResponseDto> skillDtos = new ArrayList<>();
        if (model.getSkills() != null) {
            skillDtos = model.getSkills().stream()
                    .map(skillDtoFactory::toResponseDto)
                    .collect(Collectors.toList());
        }

        return new FreelancerResponseDto(
                model.getId(),
                model.getUserName(),
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getPhoneNumber(),
                skillDtos);
    }

    /**
     * Update FreelancerModel with data from FreelancerUpdateRequestDto
     */
    public FreelancerModel updateModel(FreelancerModel model, FreelancerUpdateRequestDto dto) {
        if (model == null || dto == null)
            return model;

        if (dto.getFirstName() != null) {
            model.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            model.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            model.setPhoneNumber(dto.getPhoneNumber());
        }

        // Handle skills update if provided
        if (dto.getSkillIds() != null) {
            Set<SkillModel> skills = dto.getSkillIds().stream()
                    .map(skillService::getSkillById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            model.setSkills(skills);
        }

        return model;
    }

    /**
     * Convert a list of FreelancerModels to FreelancerResponseDtos
     */
    public List<FreelancerResponseDto> toResponseDtoList(List<FreelancerModel> models) {
        if (models == null)
            return null;
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
