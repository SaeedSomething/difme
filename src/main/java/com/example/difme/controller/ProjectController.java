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

import com.example.difme.dto.MyApiResponse;
import com.example.difme.dto.factory.ProjectDtoFactory;
import com.example.difme.dto.project.ProjectCreateRequestDto;
import com.example.difme.dto.project.ProjectCreateRequestDto;
import com.example.difme.dto.project.ProjectResponseDto;
import com.example.difme.dto.project.ProjectUpdateRequestDto;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.EmployerModel;
import com.example.difme.model.ProjectModel;
import com.example.difme.service.Employer.EmployerService;
import com.example.difme.service.Project.ProjectService;
import com.example.difme.service.auth.AuthenticationContext;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "Project management operations")
public class ProjectController {

    private ProjectService projectService;

    private EmployerService employerService;

    private ProjectDtoFactory projectDtoFactory;

    private final AuthenticationContext authenticationContext;

    @PostMapping
    @Operation(summary = "Create new project", description = "Create a new project posting")
    @ApiResponse(responseCode = "201", description = "Project created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid project data")
    @ApiResponse(responseCode = "403", description = "Only employers can create projects")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public ResponseEntity<MyApiResponse<ProjectResponseDto>> createProject(
            @Parameter(description = "Project details") @Valid @RequestBody ProjectCreateRequestDto projectDto) {
        EmployerModel employer = employerService.getEmployerById(authenticationContext.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employer not found with id: " + authenticationContext.getCurrentUserId()));
        ProjectModel project = projectDtoFactory.toModel(projectDto, employer);
        ProjectModel createdProject = projectService.createProject(project);
        ProjectResponseDto responseDto = projectDtoFactory.toResponseDto(createdProject);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MyApiResponse.success(responseDto, "Project created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve all available projects in the system")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    public ResponseEntity<MyApiResponse<List<ProjectResponseDto>>> getAllProjects() {
        List<ProjectModel> projects = projectService.getAllProjects();
        List<ProjectResponseDto> projectDtos = projectDtoFactory.toResponseDtoList(projects);
        return ResponseEntity.ok(MyApiResponse.success(projectDtos, "Projects retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by its ID")
    @ApiResponse(responseCode = "200", description = "Project found", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Project not found")
    public ResponseEntity<MyApiResponse<ProjectResponseDto>> getProjectById(
            @Parameter(description = "Project ID", example = "1") @PathVariable Long id) {
        ProjectModel project = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        ProjectResponseDto responseDto = projectDtoFactory.toResponseDto(project);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Project retrieved successfully"));
    }

    @GetMapping("/employer/{employerId}")
    @Operation(summary = "Get projects by employer", description = "Retrieve all projects posted by a specific employer")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Employer not found")
    public ResponseEntity<MyApiResponse<List<ProjectResponseDto>>> getProjectsByEmployerId(
            @Parameter(description = "Employer ID", example = "1") @PathVariable Long employerId) {
        List<ProjectModel> projects = projectService.getProjectsByEmployerId(employerId);
        List<ProjectResponseDto> projectDtos = projectDtoFactory.toResponseDtoList(projects);
        return ResponseEntity.ok(MyApiResponse.success(projectDtos, "Projects retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update an existing project")
    @ApiResponse(responseCode = "200", description = "Project updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Project not found")
    @ApiResponse(responseCode = "400", description = "Invalid project data")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<MyApiResponse<ProjectResponseDto>> updateProject(
            @Parameter(description = "Project ID", example = "1") @PathVariable Long id,
            @Parameter(description = "Updated project details") @Valid @RequestBody ProjectUpdateRequestDto projectUpdateDto) {
        ProjectModel existingProject = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        ProjectModel updatedProject = projectDtoFactory.updateModel(existingProject, projectUpdateDto);
        ProjectModel savedProject = projectService.createProject(updatedProject); // Using createProject as save method
        ProjectResponseDto responseDto = projectDtoFactory.toResponseDto(savedProject);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Project updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Delete a project from the system")
    @ApiResponse(responseCode = "200", description = "Project deleted successfully")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<MyApiResponse<Void>> deleteProject(
            @Parameter(description = "Project ID", example = "1") @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(MyApiResponse.success("Project deleted successfully"));
    }
}