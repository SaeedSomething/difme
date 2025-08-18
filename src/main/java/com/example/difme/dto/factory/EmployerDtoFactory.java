package com.example.difme.dto.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.difme.dto.employer.EmployerCreateRequestDto;
import com.example.difme.dto.employer.EmployerProfileDto;
import com.example.difme.dto.employer.EmployerResponseDto;
import com.example.difme.dto.employer.EmployerUpdateRequestDto;
import com.example.difme.model.EmployerModel;
import com.example.difme.model.enums.Role;

/**
 * Factory class for converting between Employer DTOs and EmployerModel
 */
@Component
public class EmployerDtoFactory {
    private final PasswordEncoder passwordEncoder;

    public EmployerDtoFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Convert EmployerCreateRequestDto to EmployerModel
     */
    public EmployerModel toModel(EmployerCreateRequestDto dto) {
        if (dto == null)
            return null;

        EmployerModel employer = new EmployerModel();
        employer.setUserName(dto.getUserName());
        employer.setFirstName(dto.getFirstName());
        employer.setLastName(dto.getLastName());
        employer.setEmail(dto.getEmail());
        employer.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        employer.setPhoneNumber(dto.getPhoneNumber());
        employer.setCompanyDescription(dto.getCompanyDescription());
        employer.setRole(Role.EMPLOYER);
        return employer;
    }

    /**
     * Convert EmployerModel to EmployerResponseDto
     */
    public EmployerResponseDto toResponseDto(EmployerModel model) {
        if (model == null)
            return null;

        return new EmployerResponseDto(
                model.getId(),
                model.getUserName(),
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getPhoneNumber(),
                model.getCompanyDescription());
    }

    public EmployerProfileDto toProfileDto(EmployerModel model) {
        if (model == null)
            return null;

        return new EmployerProfileDto(
                model.getUserName(),
                model.getFirstName(),
                model.getLastName(),
                model.getCompanyDescription());
    }

    /**
     * Update EmployerModel with data from EmployerUpdateRequestDto
     */
    public EmployerModel updateModel(EmployerModel model, EmployerUpdateRequestDto dto) {
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
        if (dto.getEmail() != null) {
            model.setEmail(dto.getEmail());
        }
        if (dto.getCompanyDescription() != null) {
            model.setCompanyDescription(dto.getCompanyDescription());
        }

        return model;
    }

    /**
     * Convert a list of EmployerModels to EmployerResponseDtos
     */
    public List<EmployerResponseDto> toResponseDtoList(List<EmployerModel> models) {
        if (models == null)
            return null;
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
