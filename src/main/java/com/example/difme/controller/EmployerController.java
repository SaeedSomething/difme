package com.example.difme.controller;

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
import com.example.difme.dto.employer.EmployerCreateRequestDto;
import com.example.difme.dto.employer.EmployerResponseDto;
import com.example.difme.dto.employer.EmployerUpdateRequestDto;
import com.example.difme.dto.factory.EmployerDtoFactory;
import com.example.difme.exception.ResourceNotFoundException;
import com.example.difme.model.EmployerModel;
import com.example.difme.service.Employer.EmployerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employers")
@Tag(name = "Employers", description = "Employer management operations")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private EmployerDtoFactory employerDtoFactory;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new employer", description = "Register a new employer in the system")
    @ApiResponse(responseCode = "201", description = "Employer created successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid employer data")
    public ResponseEntity<MyApiResponse<EmployerResponseDto>> createEmployer(
            @Parameter(description = "Employer details") @Valid @RequestBody EmployerCreateRequestDto employerDto) {
        EmployerModel employer = employerDtoFactory.toModel(employerDto);
        EmployerModel createdEmployer = employerService.createEmployer(employer);
        EmployerResponseDto responseDto = employerDtoFactory.toResponseDto(createdEmployer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MyApiResponse.success(responseDto, "Employer created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employer by ID", description = "Retrieve a specific employer by their ID")
    @ApiResponse(responseCode = "200", description = "Employer found", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Employer not found")
    public ResponseEntity<MyApiResponse<EmployerResponseDto>> getEmployerById(
            @Parameter(description = "Employer ID", example = "1") @PathVariable Long id) {
        EmployerModel employer = employerService.getEmployerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));
        EmployerResponseDto responseDto = employerDtoFactory.toResponseDto(employer);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Employer retrieved successfully"));
    }

    @PreAuthorize("#id==authentication.principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update employer", description = "Update an existing employer's profile")
    @ApiResponse(responseCode = "200", description = "Employer updated successfully", content = @Content(schema = @Schema(implementation = MyApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Employer not found")
    @ApiResponse(responseCode = "400", description = "Invalid employer data")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<MyApiResponse<EmployerResponseDto>> updateEmployer(
            @Parameter(description = "Employer ID", example = "1") @PathVariable Long id,
            @Parameter(description = "Updated employer details") @Valid @RequestBody EmployerUpdateRequestDto employerUpdateDto) {
        EmployerModel existingEmployer = employerService.getEmployerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));
        EmployerModel updatedEmployer = employerDtoFactory.updateModel(existingEmployer, employerUpdateDto);
        EmployerModel savedEmployer = employerService.createEmployer(updatedEmployer); // Using createEmployer as save
                                                                                       // method
        EmployerResponseDto responseDto = employerDtoFactory.toResponseDto(savedEmployer);
        return ResponseEntity.ok(MyApiResponse.success(responseDto, "Employer updated successfully"));
    }

    @PreAuthorize("#id==authentication.principal.id or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employer", description = "Delete an employer from the system")
    @ApiResponse(responseCode = "200", description = "Employer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Employer not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<MyApiResponse<Void>> deleteEmployer(
            @Parameter(description = "Employer ID", example = "1") @PathVariable Long id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.ok(MyApiResponse.success("Employer deleted successfully"));
    }
}