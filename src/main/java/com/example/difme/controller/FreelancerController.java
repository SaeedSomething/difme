package com.example.difme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.example.difme.dto.MyApiResponse;
import com.example.difme.dto.factory.FreelancerDtoFactory;
import com.example.difme.dto.factory.SkillDtoFactory;
import com.example.difme.dto.freelancer.FreelancerCreateRequestDto;
import com.example.difme.dto.freelancer.FreelancerResponseDto;
import com.example.difme.dto.freelancer.FreelancerUpdateRequestDto;
import com.example.difme.dto.skill.SkillResponseDto;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.SkillModel;
import com.example.difme.service.Freelancer.FreelancerService;
import com.example.difme.service.Freelancer.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/freelancers")
@Tag(name = "Freelancers", description = "Freelancer management operations")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private FreelancerDtoFactory freelancerDtoFactory;

    @Autowired
    private SkillDtoFactory skillDtoFactory;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new freelancer", description = "Register a new freelancer in the system")
    @ApiResponse(responseCode = "201", description = "Freelancer created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid freelancer data")
    public ResponseEntity<MyApiResponse<FreelancerResponseDto>> createFreelancer(
            @Parameter(description = "Freelancer details") @Valid @RequestBody FreelancerCreateRequestDto freelancerDto) {
        FreelancerModel freelancer = freelancerDtoFactory.toModel(freelancerDto);
        FreelancerModel createdFreelancer = freelancerService.createFreelancer(freelancer);
        FreelancerResponseDto responseDto = freelancerDtoFactory.toResponseDto(createdFreelancer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MyApiResponse.success(responseDto, "Freelancer created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get freelancer by ID", description = "Retrieve a specific freelancer by their ID")
    @ApiResponse(responseCode = "200", description = "Freelancer found", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<MyApiResponse<FreelancerResponseDto>> getFreelancerById(
            @Parameter(description = "Freelancer ID", example = "1") @PathVariable Long id) {
        FreelancerModel freelancer = freelancerService.getFreelancerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + id));
        FreelancerResponseDto responseDto = freelancerDtoFactory.toResponseDto(freelancer);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Freelancer retrieved successfully"));
    }

    @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update freelancer", description = "Update an existing freelancer's profile")
    @ApiResponse(responseCode = "200", description = "Freelancer updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    @ApiResponse(responseCode = "400", description = "Invalid freelancer data")
    public ResponseEntity<MyApiResponse<FreelancerResponseDto>> updateFreelancer(
            @Parameter(description = "Freelancer ID", example = "1") @PathVariable Long id,
            @Parameter(description = "Updated freelancer details") @Valid @RequestBody FreelancerUpdateRequestDto freelancerUpdateDto) {
        FreelancerModel existingFreelancer = freelancerService.getFreelancerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + id));
        FreelancerModel updatedFreelancer = freelancerDtoFactory.updateModel(existingFreelancer, freelancerUpdateDto);
        FreelancerModel savedFreelancer = freelancerService.createFreelancer(updatedFreelancer); // Using
                                                                                                 // createFreelancer as
                                                                                                 // save method
        FreelancerResponseDto responseDto = freelancerDtoFactory.toResponseDto(savedFreelancer);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Freelancer updated successfully"));
    }

    @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
    @PutMapping("/{id}/skills")
    @Operation(summary = "Update freelancer skills", description = "Update the skills associated with a freelancer")
    @ApiResponse(responseCode = "200", description = "Skills updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    @ApiResponse(responseCode = "400", description = "Invalid skill IDs")
    public ResponseEntity<MyApiResponse<FreelancerResponseDto>> updateFreelancerSkills(
            @Parameter(description = "Freelancer ID", example = "1") @PathVariable Long id,
            @Parameter(description = "List of skill IDs") @RequestBody List<Long> skillIds) {
        List<SkillModel> skills = skillService.getSkillsByIds(skillIds);
        FreelancerModel updatedFreelancer = freelancerService.updateFreelancerSkills(id, skills);
        FreelancerResponseDto responseDto = freelancerDtoFactory.toResponseDto(updatedFreelancer);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Skills updated successfully"));
    }

    @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete freelancer", description = "Delete a freelancer from the system")
    @ApiResponse(responseCode = "200", description = "Freelancer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<MyApiResponse<Void>> deleteFreelancer(
            @Parameter(description = "Freelancer ID", example = "1") @PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
        return ResponseEntity.ok(MyApiResponse.success("Freelancer deleted successfully"));
    }
}