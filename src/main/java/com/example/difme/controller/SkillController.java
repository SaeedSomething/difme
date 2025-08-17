package com.example.difme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.difme.dto.factory.SkillDtoFactory;
import com.example.difme.dto.skill.SkillCreateRequestDto;
import com.example.difme.dto.skill.SkillResponseDto;
import com.example.difme.dto.skill.SkillUpdateRequestDto;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.SkillModel;
import com.example.difme.service.Freelancer.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/skills")
@Tag(name = "Skills", description = "Skill management operations")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillDtoFactory skillDtoFactory;

    @GetMapping
    @Operation(summary = "Get all skills", description = "Retrieve all available skills in the system")
    @ApiResponse(responseCode = "200", description = "Skills retrieved successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    public ResponseEntity<MyApiResponse<List<SkillResponseDto>>> getAllSkills() {
        List<SkillModel> skills = skillService.getAllSkills();
        List<SkillResponseDto> skillDtos = skillDtoFactory.toResponseDtoList(skills);
        return ResponseEntity.ok(MyApiResponse.success(skillDtos, "Skills retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get skill by ID", description = "Retrieve a specific skill by its ID")
    @ApiResponse(responseCode = "200", description = "Skill found", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Skill not found")
    public ResponseEntity<MyApiResponse<SkillResponseDto>> getSkillById(
            @Parameter(description = "Skill ID", example = "1") @PathVariable Long id) {
        SkillModel skill = skillService.getSkillById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        SkillResponseDto skillDto = skillDtoFactory.toResponseDto(skill);
        return ResponseEntity.ok(MyApiResponse.success(skillDto, "Skill retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create new skill", description = "Create a new skill in the system")
    @ApiResponse(responseCode = "201", description = "Skill created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid skill data")
    public ResponseEntity<MyApiResponse<SkillResponseDto>> createSkill(
            @Parameter(description = "Skill details") @Valid @RequestBody SkillCreateRequestDto skillDto) {
        SkillModel skill = skillDtoFactory.toModel(skillDto);
        SkillModel createdSkill = skillService.createSkill(skill);
        SkillResponseDto responseDto = skillDtoFactory.toResponseDto(createdSkill);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MyApiResponse.success(responseDto, "Skill created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update skill", description = "Update an existing skill")
    @ApiResponse(responseCode = "200", description = "Skill updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Skill not found")
    @ApiResponse(responseCode = "400", description = "Invalid skill data")
    public ResponseEntity<MyApiResponse<SkillResponseDto>> updateSkill(
            @Parameter(description = "Skill ID", example = "1") @PathVariable Long id,
            @Parameter(description = "Updated skill details") @Valid @RequestBody SkillUpdateRequestDto skillUpdateDto) {
        SkillModel existingSkill = skillService.getSkillById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        SkillModel updatedSkill = skillDtoFactory.updateModel(existingSkill, skillUpdateDto);
        SkillModel savedSkill = skillService.createSkill(updatedSkill); // Using createSkill as save method
        SkillResponseDto responseDto = skillDtoFactory.toResponseDto(savedSkill);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Skill updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete skill", description = "Delete a skill from the system")
    @ApiResponse(responseCode = "200", description = "Skill deleted successfully")
    @ApiResponse(responseCode = "404", description = "Skill not found")
    public ResponseEntity<MyApiResponse<Void>> deleteSkill(
            @Parameter(description = "Skill ID", example = "1") @PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(MyApiResponse.success("Skill deleted successfully"));
    }
}