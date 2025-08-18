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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.example.difme.dto.MyApiResponse;
import com.example.difme.dto.application.ApplicationCreateWithIdsRequestDto;
import com.example.difme.dto.application.ApplicationResponseDto;
import com.example.difme.dto.application.ApplicationUpdateRequestDto;
import com.example.difme.dto.factory.ApplicationDtoFactory;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.ApplicationModel;
import com.example.difme.model.FreelancerModel;
import com.example.difme.model.ProjectModel;
import com.example.difme.model.enums.ApplicationStatus;
import com.example.difme.service.Application.ApplicationService;
import com.example.difme.service.Freelancer.FreelancerService;
import com.example.difme.service.Project.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Applications", description = "Application management operations")
public class ApplicationController {

        private ApplicationService applicationService;

        private FreelancerService freelancerService;

        private ProjectService projectService;

        private ApplicationDtoFactory applicationDtoFactory;

        @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
        @PostMapping
        @Operation(summary = "Create new application", description = "Submit an application to a project")
        @ApiResponse(responseCode = "201", description = "Application created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "400", description = "Invalid application data")
        @ApiResponse(responseCode = "403", description = "Only freelancers can apply to projects")
        public ResponseEntity<MyApiResponse<ApplicationResponseDto>> createApplication(
                        @Parameter(description = "Application details") @Valid @RequestBody ApplicationCreateWithIdsRequestDto applicationDto) {
                FreelancerModel freelancer = freelancerService.getFreelancerById(applicationDto.getFreelancerId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Freelancer not found with id: " + applicationDto.getFreelancerId()));
                ProjectModel project = projectService.getProjectById(applicationDto.getProjectId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Project not found with id: " + applicationDto.getProjectId()));
                ApplicationModel application = applicationDtoFactory.toModel(applicationDto, freelancer, project);
                ApplicationModel createdApplication = applicationService.createApplication(application);
                ApplicationResponseDto responseDto = applicationDtoFactory.toResponseDto(createdApplication);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(MyApiResponse.success(responseDto, "Application submitted successfully"));
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get application by ID", description = "Retrieve a specific application by its ID")
        @ApiResponse(responseCode = "200", description = "Application found", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "404", description = "Application not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<ApplicationResponseDto>> getApplicationById(
                        @Parameter(description = "Application ID", example = "1") @PathVariable Long id) {
                ApplicationModel application = applicationService.getApplicationById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Application not found with id: " + id));
                ApplicationResponseDto responseDto = applicationDtoFactory.toResponseDto(application);
                return ResponseEntity.ok(MyApiResponse.success(responseDto, "Application retrieved successfully"));
        }

        @GetMapping("/project/{projectId}")
        @Operation(summary = "Get applications by project", description = "Retrieve all applications for a specific project")
        @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "404", description = "Project not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<List<ApplicationResponseDto>>> getApplicationsByProjectId(
                        @Parameter(description = "Project ID", example = "1") @PathVariable Long projectId) {
                List<ApplicationModel> applications = applicationService.getApplicationsByProjectId(projectId);
                List<ApplicationResponseDto> applicationDtos = applicationDtoFactory.toResponseDtoList(applications);
                return ResponseEntity.ok(MyApiResponse.success(applicationDtos, "Applications retrieved successfully"));
        }

        @GetMapping("/freelancer/{freelancerId}")
        @Operation(summary = "Get applications by freelancer", description = "Retrieve all applications submitted by a specific freelancer")
        @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "404", description = "Freelancer not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<List<ApplicationResponseDto>>> getApplicationsByFreelancerId(
                        @Parameter(description = "Freelancer ID", example = "1") @PathVariable Long freelancerId) {
                List<ApplicationModel> applications = applicationService.getApplicationsByFreelancerId(freelancerId);
                List<ApplicationResponseDto> applicationDtos = applicationDtoFactory.toResponseDtoList(applications);
                return ResponseEntity.ok(MyApiResponse.success(applicationDtos, "Applications retrieved successfully"));
        }

        @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
        @PutMapping("/{id}/status")
        @Operation(summary = "Update application status", description = "Update the status of an application (only by project owner)")
        @ApiResponse(responseCode = "200", description = "Application status updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "404", description = "Application not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<ApplicationResponseDto>> updateApplicationStatus(
                        @Parameter(description = "Application ID", example = "1") @PathVariable Long id,
                        @Parameter(description = "New application status") @RequestParam ApplicationStatus status) {
                ApplicationModel updatedApplication = applicationService.updateApplicationStatus(id, status);
                ApplicationResponseDto responseDto = applicationDtoFactory.toResponseDto(updatedApplication);
                return ResponseEntity.ok(MyApiResponse.success(responseDto, "Application status updated successfully"));
        }

        @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
        @PutMapping("/{id}")
        @Operation(summary = "Update application", description = "Update application details (only by applicant)")
        @ApiResponse(responseCode = "200", description = "Application updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
        @ApiResponse(responseCode = "404", description = "Application not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<ApplicationResponseDto>> updateApplication(
                        @Parameter(description = "Application ID", example = "1") @PathVariable Long id,
                        @Parameter(description = "Updated application details") @Valid @RequestBody ApplicationUpdateRequestDto applicationUpdateDto) {
                ApplicationModel existingApplication = applicationService.getApplicationById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Application not found with id: " + id));
                ApplicationModel updatedApplication = applicationDtoFactory.updateModel(existingApplication,
                                applicationUpdateDto);
                ApplicationModel savedApplication = applicationService.createApplication(updatedApplication); // Using
                                                                                                              // createApplication
                                                                                                              // as save
                                                                                                              // method
                ApplicationResponseDto responseDto = applicationDtoFactory.toResponseDto(savedApplication);
                return ResponseEntity.ok(MyApiResponse.success(responseDto, "Application updated successfully"));
        }

        @PreAuthorize("#id==@authenticationContext.getCurrentUser().getId() or hasRole('ADMIN')")
        @DeleteMapping("/{id}")
        @Operation(summary = "Delete application", description = "Delete an application (only by applicant)")
        @ApiResponse(responseCode = "200", description = "Application deleted successfully")
        @ApiResponse(responseCode = "404", description = "Application not found")
        @ApiResponse(responseCode = "403", description = "Access denied")
        public ResponseEntity<MyApiResponse<Void>> deleteApplication(
                        @Parameter(description = "Application ID", example = "1") @PathVariable Long id) {
                applicationService.deleteApplication(id);
                return ResponseEntity.ok(MyApiResponse.success("Application deleted successfully"));
        }
}